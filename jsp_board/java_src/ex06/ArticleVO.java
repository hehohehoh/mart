package ex06;

import java.sql.Timestamp;

public class ArticleVO {

	private int level;
	private int articleNO;
	private int parentNO;
	private String title;
	private String content;
	private String id;
	private String imageFileName;
	private Timestamp writeDate;
	
	public ArticleVO() {
		super();
	}

	public ArticleVO(int articleNO, int parentNO, String title, String content, String id) {
		super();
		this.articleNO = articleNO;
		this.parentNO = parentNO;
		this.title = title;
		this.content = content;
		this.id = id;
	}

	public ArticleVO(int level, int articleNO, int parentNO, String title, String content, String id,
			Timestamp writeDate) {
		super();
		this.level = level;
		this.articleNO = articleNO;
		this.parentNO = parentNO;
		this.title = title;
		this.content = content;
		this.id = id;
		this.writeDate = writeDate;
	}

	public ArticleVO(int level, int articleNO, int parentNO, String title, String content, String id,
			String imageFileName, Timestamp writeDate) {
		super();
		this.level = level;
		this.articleNO = articleNO;
		this.parentNO = parentNO;
		this.title = title;
		this.content = content;
		this.id = id;
		this.imageFileName = imageFileName;
		this.writeDate = writeDate;
	}
	
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getArticleNO() {
		return articleNO;
	}

	public void setArticleNO(int articleNO) {
		this.articleNO = articleNO;
	}

	public int getParentNO() {
		return parentNO;
	}

	public void setParentNO(int parentNO) {
		this.parentNO = parentNO;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public Timestamp getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Timestamp writeDate) {
		this.writeDate = writeDate;
	}
	
	
}
