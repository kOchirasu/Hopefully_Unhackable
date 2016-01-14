package mongodb;
import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
import java.util.List;
import com.mongodb.BasicDBList;


public class InvertedIndex {
	
	

	@Id
	private String keyword;
	
	private String file_id;
	
	private BasicDBList file_list;
	
	
	public InvertedIndex(){}
	
	public InvertedIndex(String keyword, BasicDBList file_list) {
		this.keyword = keyword;
		this.file_list = file_list;
	}
	
	public BasicDBList getList(){
		return this.file_list;
	}
	
	
	@Override
	public String toString() {
		return String.format(
				"InvertedIndex[id='%s', file_id='%s']",
				keyword, file_list);
	}
	
	public static void store(String keyword, String file_id){

		
	}
}