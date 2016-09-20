package com.epam.nv.processor;

import com.epam.nv.io.IdReader;
import com.epam.nv.io.IdWriter;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Nikolay_Vasilishin on 5/24/2016.
 */
public class IdMerger {
    //    private ExecutorService pool;
    private final FileSystem fileSystem;
    private Path tmpDir;
    private Path output;
    private BlockingQueue<Path> queue;

    public IdMerger(FileSystem fileSystem, Path tmpDir, Path output) {
        this.fileSystem = fileSystem;
        this.tmpDir = tmpDir;
        this.output = output;
        queue = new PriorityBlockingQueue<>(50, new Comparator<Path>() {
            @Override
            public int compare(Path o1, Path o2) {
                try {
                    long delta = fileSystem.getFileStatus(o1).getLen() - fileSystem.getFileStatus(o2).getLen();
                    return (int) Math.signum(delta);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
//        pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        Logger.getLogger(IdMerger.class.toString()).info("Merger was instantiated.");

    }

    public void mergeFiles() {
        while (queue.size() != 1) {
            try {
//                pool.execute(new Merger(queue.take(), queue.take()));
                new Merger(queue.take(), queue.take()).run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Logger.getLogger(IdMerger.class.toString()).info("All files were merged. Copying the result file to " + output + " path.");
//        pool.shutdown();
        try {
            fileSystem.rename(queue.take(), output);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class Merger implements Runnable {
        private Path first;
        private Path second;
        private String s1;
        private String s2;

        public Merger(Path first, Path second) {
            this.first = first;
            this.second = second;
        }

        private void merge() {
            try (IdReader firstFile = new IdReader().init(fileSystem, false, first);
                 IdReader secondFile = new IdReader().init(fileSystem, false, second);
                 IdWriter writer = new IdWriter(fileSystem)) {

                writer.setTmpDir(tmpDir);
                Path path = writer.openStream();

                //read first lines
                if (firstFile.hasNext()) {
                    s1 = firstFile.next() + "\n";
                }
                if (secondFile.hasNext()) {
                    s2 = secondFile.next() + "\n";
                }

                while (firstFile.hasNext() && secondFile.hasNext()) {
                    //if first line is gt second, write it and read next line from first file
                    if (Long.valueOf(s1.split("\\s+")[1]).compareTo(Long.valueOf(s2.split("\\s+")[1])) > 0) {
                        writer.write(s1);
                        s1 = firstFile.next() + "\n";
                    } else if (Long.valueOf(s1.split("\\s+")[1]).compareTo(Long.valueOf(s2.split("\\s+")[1])) == 0) {
                        String line = s1.split("\\s+")[0] + " " + (Long.valueOf(s1.split("\\s+")[1]) + Long.valueOf(s2.split("\\s+")[1]));
                        writer.write(line + "\n");
                        s1 = firstFile.next() + "\n";
                        s2 = secondFile.next() + "\n";
                    } else {
                        writer.write(s2);
                        s2 = secondFile.next() + "\n";
                    }
                }

                //write rest of other file
                while (firstFile.hasNext()) {
                    writer.write(firstFile.next());
                }

                while (secondFile.hasNext()) {
                    writer.write(secondFile.next());
                }
                queue.put(path);
                Logger.getLogger(IdMerger.class.toString()).debug("Files " + first + ", " + second + " were merged into " + path);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger(IdMerger.class.toString()).info("Failed with lines: \n" + s1 + "\n" + s2);

            }

            try {
                fileSystem.delete(first, false);
                fileSystem.delete(second, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            merge();
        }
    }

    public void addFile(Path file) {
        queue.add(file);
    }


}
