package com.kerbybit.chattriggers.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.triggers.StringHandler;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//TODO bad class name. pls change
public class NewJsonHandler {
    private static JsonObject getJsonFromURL(String url) {
        try {
            String jsonString = "";
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream(),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                jsonString+=line;
            }
            bufferedReader.close();

            return new JsonParser().parse(jsonString).getAsJsonObject();
        } catch (MalformedURLException e1) {
            return null;
        } catch (IOException e2) {
            return null;
        }
    }

    private static JsonObject getJsonFromFile(String dest) {
        try {
            String jsonString = "";
            String line;
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                jsonString += line;
            }
            return new JsonParser().parse(jsonString).getAsJsonObject();
        } catch (IOException e1) {
            return null;
        }
    }

    private static String getJson(String json_name, String json_from) {
        JsonObject json_object;
        if (json_from.toUpperCase().startsWith("HTTP")) {
            json_object = getJsonFromURL(json_from);
        } else {
            json_object = getJsonFromFile(json_from);
        }

        if (json_object == null) {
            return "Unable to load Json!";
        } else {
            global.jsons.put(json_name, json_object);
            return json_object + "";
        }
    }

    private static String getValue(String key, String value) {
        if (global.jsons.containsKey(key)) {
            try {
                JsonObject obj = global.jsons.get(key);
                String[] seg = value.split("\\.");
                String returnString = "null";
                for (String element : seg) {
                    if (obj != null) {
                        JsonElement ele = obj.get(element);
                        if (!ele.isJsonObject()) {
                            returnString = ele + "";
                        } else {
                            obj = ele.getAsJsonObject();
                            returnString = obj + "";
                        }
                    } else {
                        return "null";
                    }
                }
                if (returnString.startsWith("\"") && returnString.endsWith("\"")) {
                    returnString = returnString.substring(1, returnString.length()-1);
                }
                return returnString;
            } catch (Exception e) {
                e.printStackTrace();
                return "null";
            }
        } else {
            return "Not a json";
        }
    }

    private static void clearJsons() {
        global.jsons.clear();
    }

    private static String clearJson(String name) {
        if (global.jsons.containsKey(name)) {
            global.jsons.remove(name);
            return "Cleared json " + name;
        } else {
            return "No json by the name of " + name + " to clear";
        }
    }

    public static String jsonFunctions(String TMP_e) {
        while (TMP_e.contains("{json[") && TMP_e.contains("]}.clear()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{json[")+6, TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{json[")));

            List<String> temporary = new ArrayList<String>();
            temporary.add("JsonToString->"+get_name+"CLEAR-"+(global.TMP_string.size()+1));
            temporary.add(clearJson(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{json["+get_name+"]}.clear()","{string[JsonToString->"+get_name+"CLEAR-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.load(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{json[")+6, TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json["))+8, TMP_e.indexOf(")", TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json["))));
            while (get_prevalue.contains("(")) {
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{jaon["))+8);
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);

            List<String> temporary = new ArrayList<String>();
            temporary.add("JsonToString->"+get_name+"LOAD"+get_value+"-"+(global.TMP_string.size()+1));
            temporary.add(getJson(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{json["+get_name+"]}.load("+get_prevalue+")","{string[JsonToString->"+get_name+"LOAD"+get_value+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.get(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{json[")+6, TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json["))+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json["))));
            while (get_prevalue.contains("(")) {
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.get(", TMP_e.indexOf("{jaon["))+8);
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);

            List<String> temporary = new ArrayList<String>();
            temporary.add("JsonToString->"+get_name+"GET"+get_value+"-"+(global.TMP_string.size()+1));
            temporary.add(getValue(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{json["+get_name+"]}.get("+get_prevalue+")","{string[JsonToString->"+get_name+"GET"+get_value+"-"+global.TMP_string.size()+"]}");
        }

        return TMP_e;
    }
}
