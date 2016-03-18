package org.simpumind.com.schooloftechyctcoursebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by simpumind on 10/3/15.
 */
public class ExpandableListDataPump {

        public static HashMap<String, List<String>> getData() {
            HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

            List<String> semsester = new ArrayList<String>();
            semsester.add("First Semester");
            semsester.add("Second Semester");

            expandableListDetail.put("Nation Diploma (ND 1)", semsester);
            expandableListDetail.put("Nation Diploma (ND 2)", semsester);
            expandableListDetail.put("Higher Nation Diploma (HND 1)", semsester);
            expandableListDetail.put("Higher Nation Diploma (HND 2)", semsester);
            return expandableListDetail;
        }
}
