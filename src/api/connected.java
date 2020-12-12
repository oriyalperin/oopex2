package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class connected {
    private HashMap<Integer,ver>f;
    public connected(Collection<node_data> nodes)
    {
        this.f=new HashMap<>();
        for(node_data nd: nodes)
        {
            int n=nd.getKey();
            f.put(n,new ver(n));
        }


    }
    public HashMap<Integer,ver> getHM()
    {
        return f;
    }
    public ver getNode(int key)
    {
        return f.get(key);
    }
    public static class ver
    {
      private int key;
       private int low;
        private int disc;
        private boolean sm;


        public ver(int key)
        {
            this.key=key;

        }

        public int getKey() {
            return key;
        }

        public int getLow() {
            return low;
        }

        public void setLow(int low) {
            this.low = low;
        }

        public int getDisc() {
            return disc;
        }

        public void setDisc(int disc) {
            this.disc = disc;
        }

        public boolean isSm() {
            return sm;
        }

        public void setSm(boolean sm) {
            this.sm = sm;
        }
    }


}
