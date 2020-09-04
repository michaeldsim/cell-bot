package Other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.json.JSONObject;

public class JSONParse {

	public static Map<String, String> championList = new HashMap<String, String>();
	static Properties prop = new Properties();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			File file = new File("champion.json");
			String json = new Scanner(file, "UTF-8").useDelimiter("\\Z").next();
			System.out.println(json);

			JSONObject obj = new JSONObject((String) json);
			JSONObject data = obj.getJSONObject("data");

			Iterator<String> keys = data.keys();

			while (keys.hasNext()) {
				String key = keys.next();
				if (data.get(key) instanceof JSONObject) {
					JSONObject champion = (JSONObject) data.get(key);
					System.out.println("Name: " + champion.getString("name"));
					System.out.println("key: " + champion.getString("key"));
					championList.put(champion.getString("key"), champion.getString("name"));
				}
			}

			save();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void save() {
		prop.putAll(championList);
		try {
			prop.store(new FileOutputStream("champion.properties", false), null);
		} catch (IOException e) {
			System.out.println("There was an error with saving!");
			e.printStackTrace();
		}

	}

	public static void load() {
		try {
			prop.load(new FileInputStream("champion.properties"));
			Map props = new Properties();
			props = prop;

			championList = new HashMap<String, String>(props);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("There was an error with loading!");
			e.printStackTrace();
		}
	}

	public static Map<String, String> getList() {
		load();
		return championList;
	}

}
