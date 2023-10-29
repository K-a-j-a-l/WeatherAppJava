package MyPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String cityName=request.getParameter("cityName");
		//System.out.print(cityName);
		String api_key="8c7352e7f26adb9b1948a52710c3e92b";
		String api_url="https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+api_key;
		try {
			URL url=new URL(api_url);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			InputStream inputStream=connection.getInputStream();
			InputStreamReader reader=new InputStreamReader(inputStream);
			
			StringBuilder responseContent=new StringBuilder();
			Scanner sc=new Scanner(reader);
			while(sc.hasNext()) {
				responseContent.append(sc.nextLine());
			}
			sc.close();
			Gson gson = new Gson();
	        JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
	        
	        //Date & Time
	        long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
	        String date = new Date(dateTimestamp).toString(); 
	        
	        //Temperature
	        double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
	        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
	       
	        //Humidity
	        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
	        
	        //Wind Speed
	        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
	        
	        //Weather Condition
	        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
	        
	        // Set the data as request attributes (for sending to the jsp page)
	        request.setAttribute("date", date);
	        request.setAttribute("city", cityName);
	        request.setAttribute("temperature", temperatureCelsius);
	        request.setAttribute("weatherCondition", weatherCondition); 
	        request.setAttribute("humidity", humidity);    
	        request.setAttribute("windSpeed", windSpeed);
	        request.setAttribute("weatherData", responseContent.toString());
	        
	        connection.disconnect();	
		}catch(IOException e) {
			e.printStackTrace();
		}
        request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
