package com.nv.hadoop;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;

public class StreamingTextOutputFormat<K, V> extends TextOutputFormat<K, V> {

	protected static class StreamingLineRecordWriter<K, V> extends
			RecordWriter<K, V> {
		private K previousKey;
		private static final String utf8 = "UTF-8";
		private static final byte[] newline;

		// get UTF-8
		static {
			try {
				newline = "\n".getBytes(utf8);
			} catch (UnsupportedEncodingException uee) {
				throw new IllegalArgumentException("can't find " + utf8
						+ " encoding");
			}
		}

		protected DataOutputStream out;
		private final byte[] keyValueSeparator;
		private final byte[] valueDelimiter;
		private boolean dataWritten = false;

		public StreamingLineRecordWriter(DataOutputStream out,
				String keyValueSeparator, String valueDelimiter) {
			this.out = out;
			try {
				this.keyValueSeparator = keyValueSeparator.getBytes(utf8);
				this.valueDelimiter = valueDelimiter.getBytes(utf8);
			} catch (UnsupportedEncodingException uee) {
				throw new IllegalArgumentException("can't find " + utf8
						+ " encoding");
			}
		}

		public StreamingLineRecordWriter(DataOutputStream out) {
			this(out, ": ", "\n\t");
		}

		/**
		 * Write the object to the byte stream, handling Text as a special case.
		 *
		 * @param o
		 *            the object to print
		 * @throws IOException
		 *             if the write throws, we pass it on
		 */
		private void writeObject(Object o) throws IOException {
			if (o instanceof Text) {
				Text to = (Text) o;
				out.write(to.getBytes(), 0, to.getLength());
			} else {
				out.write(o.toString().getBytes(utf8));
			}
		}

		@Override
		public synchronized void write(K key, V value) throws IOException {

//			if (previousKey != null)
//				// if we've written data before, append a new line
//				out.write(newline);

			if (previousKey == null || !previousKey.equals(key)) {
				// write out the key and separator
				out.write(newline);
				writeObject(key);
				out.write(keyValueSeparator);
			}


			// write out the value
			writeObject(value);
			out.write(valueDelimiter);
			// track that we've written some data
			dataWritten = true;
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			// if we've written out any data, append a closing newline
			if (dataWritten) {
				out.write(newline);
			}

			out.close();
		}
	}

	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		Configuration conf = job.getConfiguration();
		boolean isCompressed = getCompressOutput(job);
		String keyValueSeparator = conf.get(
				"mapreduce.textoutputformat.separator", "\t");
		String valueDelimiter = conf.get(
				"mapreduce.textoutputformat.delimiter", ",");
		if (!isCompressed) {
			Path file = FileOutputFormat.getOutputPath(job);
			FileSystem fs = file.getFileSystem(conf);
			FSDataOutputStream fileOut = fs.create(file, job);
			return new StreamingLineRecordWriter<K, V>(fileOut,
					keyValueSeparator, valueDelimiter);
		} else {
			Class<? extends CompressionCodec> codecClass = getOutputCompressorClass(
					job, GzipCodec.class);
			// create the named codec
			CompressionCodec codec = ReflectionUtils.newInstance(codecClass,
					conf);
			// build the filename including the extension
			Path file = FileOutputFormat.getOutputPath(job);
			FileSystem fs = file.getFileSystem(conf);
			FSDataOutputStream fileOut = fs.create(file, job);
			return new StreamingLineRecordWriter<K, V>(new DataOutputStream(
					codec.createOutputStream(fileOut)), keyValueSeparator,
					valueDelimiter);
		}
	}
}