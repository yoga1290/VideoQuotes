package videoquotes.repository;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import javax.jdo.Query;

import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;

@Service
public class GUserRepository extends JDOCrudRepository<GUser, String>{

	public GUserRepository() {
		super(GUser.class);
	}
	
	
	@SuppressWarnings("unchecked")
	public Collection<GUser> findById(String id){
		Query query = PMF.get().getPersistenceManager().newQuery(GUser.class);
		query.setFilter("id == n");
		query.declareParameters("String n");
		return (List<GUser>)query.execute(id);
	}
	
	public GUser findByAccessToken(String access_token) {
		String facebookId=readText("https://graph.facebook.com/me?access_token="+access_token);
		facebookId=facebookId.substring(facebookId.indexOf("\"id\":\"")+6,facebookId.indexOf("\","));
		return findOne(facebookId);
	}
	private String readText(String uri)
	{
		String res="";
		try{
			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream in=connection.getInputStream();
			byte buff[]=new byte[200];
			int ch;
			while((ch=in.read(buff))!=-1)
					res+=new String(buff,0,ch);
			in.close();
			
			return res;
		}catch(Exception e){return  e.getLocalizedMessage();}
	}
}