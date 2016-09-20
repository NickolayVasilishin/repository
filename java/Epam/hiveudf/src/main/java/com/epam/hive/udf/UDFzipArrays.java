package com.epam.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ListObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StandardMapObjectInspector;


/**
 * Map 2 arrays of equal lengths
 */
public class UDFzipArrays extends GenericUDF {
    private ListObjectInspector listInspector1;
    private ListObjectInspector listInspector2;
    private StandardMapObjectInspector returnInspector;


    @Override
    public Object evaluate(DeferredObject[] arg0) throws HiveException {
        Object list1 = arg0[0].get();
        Object list2 = arg0[1].get();
        int listSize = listInspector1.getListLength(list1);
        Object map = returnInspector.create();
        
        for(int i = 0; i < listSize; i ++) {
        	returnInspector.put(map, listInspector1.getListElement(list1, i), listInspector1.getListElement(list2, i));
        }
        return map;
    }


    @Override
    public String getDisplayString(String[] arg0) {
        return "truncate_array(" + arg0[0] + ", " + arg0[1] + " )";
    }


    @Override
    public ObjectInspector initialize(ObjectInspector[] arg0)
            throws UDFArgumentException {
        ObjectInspector first = arg0[0];
        if (first.getCategory() == Category.LIST) {
            listInspector1 = (ListObjectInspector) first;
        } else {
            throw new UDFArgumentException(" Expecting an array and an int as arguments ");
        }
        ObjectInspector second = arg0[1];
        if (first.getCategory() == Category.LIST) {
            listInspector2 = (ListObjectInspector) second;
        } else {
            throw new UDFArgumentException(" Expecting an array and an int as arguments ");
        }
        
        returnInspector = ObjectInspectorFactory.getStandardMapObjectInspector(listInspector1.getListElementObjectInspector(), listInspector2.getListElementObjectInspector());
        return returnInspector;
    }

}