package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map.Entry;


public class json_to_graph implements JsonDeserializer<directed_weighted_graph> {
    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsongraph =jsonElement.getAsJsonObject();
        directed_weighted_graph g=new DWGraph_DS();
        int edgeCount = jsongraph.get("edgeCount").getAsInt();
        JsonObject nodesjson =jsongraph.get("Nodes").getAsJsonObject();
        JsonObject edgesjson =jsongraph.get("Edges").getAsJsonObject();
        for (Entry<String,JsonElement> set :nodesjson.entrySet()) {
            JsonElement nodej = set.getValue();
            int key = nodej.getAsJsonObject().get("id").getAsInt();
            int tag = nodej.getAsJsonObject().get("tag").getAsInt();
            double w = nodej.getAsJsonObject().get("weight").getAsDouble();
           // boolean sm = nodej.getAsJsonObject().get("sm").getAsBoolean();
            String in = nodej.getAsJsonObject().get("Info").getAsString();
            //int low = nodej.getAsJsonObject().get("low").getAsInt();
           // int disc = nodej.getAsJsonObject().get("disc").getAsInt();
            int pos = nodej.getAsJsonObject().get("pos").getAsInt();
            node_data n=new DWGraph_DS.NodeData(key,pos,tag,w,in);
            g.addNode(n);

        }
        for(Entry<String,JsonElement> set :edgesjson.entrySet())
        {
            JsonElement edges = set.getValue();

            for(Entry<String,JsonElement>setedges  :edges.getAsJsonObject().entrySet()) {
                String destKey = setedges.getKey();
                JsonElement edgej = setedges.getValue();
                int src = edgej.getAsJsonObject().get("src").getAsInt();
                int dest = edgej.getAsJsonObject().get("dest").getAsInt();
                double w = edgej.getAsJsonObject().get("w").getAsDouble();
                DWGraph_DS.EdgeData e = new DWGraph_DS.EdgeData(src, dest, w);
                g.connect(src, dest, w);
            }

        }

return g;
    }
}
