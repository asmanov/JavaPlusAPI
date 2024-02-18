import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        String end_date = "2023-02-01";
//        String start_date = "2023-01-01";
        try {
            String msg = JOptionPane.showInputDialog("Enter your message");
            msg = msg.replace(' ', '+');
            //"https://pixabay.com/api/?key=34119621-cd62b9995da20c163b58de4f7&q=cars+green&image_type=photo&pretty=true"
            //"https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&api_key=DEMO_KEY"
            //URL url = new URL("https://api.nasa.gov/planetary/apod?date="+msg+"&api_key=DEMO_KEY");
            URL url = new URL("https://pixabay.com/api/?key=34119621-cd62b9995da20c163b58de4f7&q="+msg+"&image_type=photo&pretty=true");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();
            System.out.println(responsecode);

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                //Using the JSON simple library parse the string into a json object
                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                System.out.println(data_obj);
                int i = 1;
                JSONArray arr = (JSONArray) data_obj.get("hits");
                arr.stream().forEach(f-> {
                    JSONObject newo = (JSONObject) f;
                    //System.out.println(newo);
                    String webURL = (String) newo.get("webformatURL");
                    String id = newo.get("id").toString();
                    String dirName = "C:\\Users\\ado_1\\Downloads";
                    try {
                        URL urlitem = new URL(webURL);
                        String fileName = id + ".jpg";
                        String fullName = dirName + File.separator + fileName;
                        Path destination = Paths.get(fullName);
                        Files.copy(urlitem.openStream(), destination);
                        System.out.println("Image was saved");

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
//                for (int i = 0; i < arr.size(); i++) {
//
//                    JSONObject new_obj = (JSONObject) arr.get(i);
//
//                    System.out.println(new_obj.get(""));
//                }

//
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}