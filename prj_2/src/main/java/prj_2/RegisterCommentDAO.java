package prj_2;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 댓글 문의&답변과 수정, 삭제, 채택 기능을 구현한 클래스
 * @author user
 */
public class RegisterCommentDAO {
	
	/**
	 * 댓글 작성 메소드
	 */
	public void insertCom(CommVO cVO)throws SQLException {
		
		Connection con=null;
		PreparedStatement pstmt=null;
		
		DbConnection dbCon=DbConnection.getInstance();
		try {
		//1. JNDI 사용객체 생성
		//2. DateSource를 얻어주기
		//3. Connection 얻기
			con=dbCon.getConn();
		//4. 쿼리문 생성객체 얻기
			StringBuilder insertComm=new StringBuilder();
			insertComm
			.append("insert into PROD_COMMENT(COMMENT_NUM,USER_ID,PROD_COMMENTS,WRITE_DATE,PROD_NUM) ")
			.append("values(GANGNAMKONG.COMMENT_NUM_SEQ.NEXTVAL,?,?,sysdate,?)		");
			
			pstmt=con.prepareStatement(insertComm.toString());
		//5. 바인드 변수 값 설정
			pstmt.setString(1,cVO.getUserId());
			pstmt.setString(2,cVO.getProdComments());
			pstmt.setInt(3,cVO.getProdNum());
			
			pstmt.executeQuery();
		//6. 쿼리문 수행 후 결과 얻기
		}finally {
		//7. 연결끊기
			dbCon.dbClose(null, pstmt, con);		
		}//end finally
	}//insertCom
	
	
	
	/**
	 * 해당 유저가 댓글을 달 자격이 되는지 확인
	 * @param rVO
	 * @return
	 * @throws SQLException
	 */
	public Boolean checkReply(String userId, int prodNum, int commNum) throws SQLException {
		
		Boolean result=false;
		
		Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs=null;
	    
	    DbConnection dbCon = DbConnection.getInstance();
	    try {
	        con = dbCon.getConn();
	    //쿼리문 생성객체 작성 필요.
	        
	        StringBuilder commList = new StringBuilder();        		     		        
	        commList
	        .append("	select COMMENT_NUM, pc.USER_ID, pc.PROD_NUM,p.USER_ID as product_user_id										")
	        .append("	from PROD_COMMENT pc,PRODUCT p																								")
	        .append("	where p.PROD_NUM=pc.PROD_NUM and pc.prod_num=? and comment_num=? and (pc.user_id=? or p.user_id=?)					");

	        
	        pstmt=con.prepareStatement(commList.toString());
	        pstmt.setInt(1, prodNum);
	        pstmt.setInt(2, commNum);
	        pstmt.setString(3, userId);
	        pstmt.setString(4, userId);
	        
	        rs=pstmt.executeQuery();

	        if(rs.next()) {
	        	result=true;
	        }//end while
	         
	    } finally {
	        dbCon.dbClose(rs,pstmt, con);
	    }//end finally	
		
		return result;
	}
	
	
	
	/**
	 * 댓글을 수정하는 메소드
	 */
	public void updateComm(CommVO cVO)throws SQLException {
		
		Connection con=null;
		PreparedStatement pstmt=null;
		
		DbConnection dbCon=DbConnection.getInstance();
		
		try {
		//1. JNDI 사용객체 생성
		//2. DateSource를 얻어주기
		//3. Connection 얻기
			con=dbCon.getConn();
		//4. 쿼리문 생성객체 얻기
			StringBuilder updateCom=new StringBuilder();
			updateCom
			.append("		update PROD_COMMENT 	")
			.append("		set  	 PROD_COMMENTS=?	")
			.append("		where  COMMENT_NUM=? 	");
			
			pstmt=con.prepareStatement(updateCom.toString());
		//5. 바인드 변수 값 설정
			pstmt.setString(1, cVO.getProdComments());
			pstmt.setInt(2, cVO.getCommNum());
			
			pstmt.executeQuery();
		//6. 쿼리문 수행 후 결과 얻기
		}finally {
		//7. 연결끊기
			dbCon.dbClose(null, pstmt, con);	
		}//end finally
	}//updateComm
	
	/**
	 * 댓글을 삭제하는 메소드
	 */
	public void deleteComm(CommVO cVO)throws SQLException {
		Connection con=null;
		PreparedStatement pstmt=null;
		
		DbConnection dbCon=DbConnection.getInstance();
		
		try {
		//1. JNDI 사용객체 생성
		//2. DateSource를 얻어주기
		//3. Connection 얻기
			con=dbCon.getConn();
		//4. 쿼리문 생성객체 얻기
			String deleteCom="delete from PROD_COMMENT where COMMENT_NUM=?";
			
			pstmt=con.prepareStatement(deleteCom);
			
			//5. 바인드 변수 값 설정
			pstmt.setInt(1, cVO.getCommNum());
			//6. 쿼리문 수행 후 결과 얻기
			pstmt.executeQuery();
		}finally {
		//7. 연결끊기
			dbCon.dbClose(null, pstmt, con);	
		}//end finally
	}//deleteComm
	
	//===========================답변글 메소드==========================================//
	/**
	 * 답변글 작성 메소드
	 */
	public void insertReply(ReplyCommVO rcVO)throws SQLException {
		
		Connection con=null;
		PreparedStatement pstmt=null;
		
		DbConnection dbCon=DbConnection.getInstance();
		try {
			//1. JNDI 사용객체 생성
			//2. DateSource를 얻어주기
			//3. Connection 얻기
			con=dbCon.getConn();
			//4. 쿼리문 생성객체 얻기
			StringBuilder insertComm=new StringBuilder();
			insertComm
			.append("insert into REPLY_COMMENT(REPLY_NUM, REPLY_COMMENT, WRITE_DATE, COMMENT_NUM, USER_ID ) ")
			.append("values(GANGNAMKONG.REPLY_NUM_SEQ.NEXTVAL,?,sysdate,?,?)		");
			
			pstmt=con.prepareStatement(insertComm.toString());
			//5. 바인드 변수 값 설정
			pstmt.setString(1,rcVO.getReplyCom());
			pstmt.setInt(2,rcVO.getcommNum());
			pstmt.setString(3,rcVO.getUserId());
			
			pstmt.executeQuery();
			//6. 쿼리문 수행 후 결과 얻기
		}finally {
			//7. 연결끊기
			dbCon.dbClose(null, pstmt, con);		
		}//end finally
	}//insertCom
	
	/**
	 * 답변글을 수정하는 메소드
	 */
	public void updateReply(ReplyCommVO rcVO)throws SQLException {
		
		Connection con=null;
		PreparedStatement pstmt=null;
		
		DbConnection dbCon=DbConnection.getInstance();
		
		try {
			//1. JNDI 사용객체 생성
			//2. DateSource를 얻어주기
			//3. Connection 얻기
			con=dbCon.getConn();
			//4. 쿼리문 생성객체 얻기
			StringBuilder updateCom=new StringBuilder();
			updateCom
			.append("		update 	REPLY_COMMENT 											")
			.append("		set     REPLY_COMMENT=?												")
			.append("		where  	REPLY_NUM=? and COMMENT_NUM=? 		");

			pstmt=con.prepareStatement(updateCom.toString());
			//5. 바인드 변수 값 설정
		
			pstmt.setString(1, rcVO.getReplyCom());
			pstmt.setInt(2, rcVO.getRepNum());
			pstmt.setInt(2, rcVO.getcommNum());
			
			pstmt.executeQuery();
			//6. 쿼리문 수행 후 결과 얻기
		}finally {
			//7. 연결끊기
			dbCon.dbClose(null, pstmt, con);	
		}//end finally
	}//updateComm
	
	/**
	 * 답변글을 삭제하는 메소드
	 */
	public void deleteReply(ReplyCommVO rcVO)throws SQLException {
		Connection con=null;
		PreparedStatement pstmt=null;
		
		DbConnection dbCon=DbConnection.getInstance();
		
		try {
		//1. JNDI 사용객체 생성
		//2. DateSource를 얻어주기
		//3. Connection 얻기
			con=dbCon.getConn();
		//4. 쿼리문 생성객체 얻기
			String deleteCom="delete	from    REPLY_COMMENT	where  	REPLY_NUM=?	and COMMENT_NUM=?";
			
			pstmt=con.prepareStatement(deleteCom);
			
			//5. 바인드 변수 값 설정
			pstmt.setInt(1, rcVO.getRepNum());
			pstmt.setInt(2, rcVO.getcommNum());
			//6. 쿼리문 수행 후 결과 얻기
			pstmt.executeQuery();
		}finally {
		//7. 연결끊기
			dbCon.dbClose(null, pstmt, con);	
		}//end finally
	}//deleteComm
	
	
	
	
	
	/**
	 * 댓글을 채택하는 메소드
	 * 관심목록,구매목록,판매목록,상품상태 업데이트 필요(4개 필요).
	 */
	public void updateTablePrd(ProdConditionVO pcVO) throws SQLException {
	    Connection con = null;
	    PreparedStatement pstmt = null;

	    DbConnection dbCon = DbConnection.getInstance();
	    try {
	        con = dbCon.getConn();

	        // 구매목록 테이블 업데이트
	        if(!pcVO.getSellerId().equals("없음")) {
	        StringBuilder updateOnSaleList = new StringBuilder();
	        updateOnSaleList
	        .append("insert into PURCHASE_LIST(PROD_NUM,SELLER_ID, USER_ID) values(?,?,?)");
	        pstmt = con.prepareStatement(updateOnSaleList.toString());
			pstmt.setInt(1,pcVO.getProdNum()); //상품번호
			pstmt.setString(2,pcVO.getUserId()); //파는사람
			pstmt.setString(3,pcVO.getSellerId()); //산사람
	        pstmt.executeQuery();	        
	        pstmt.close();
	        }
	        
	        // 판매목록 테이블 업데이트
	        if(!pcVO.getSellerId().equals("없음")) {
		        StringBuilder updateSoldList = new StringBuilder();
		        updateSoldList
		        .append("insert into SALES_LIST(PROD_NUM,PURCHASER_ID, USER_ID) values(?,?,?)");
		        pstmt = con.prepareStatement(updateSoldList.toString());
				pstmt.setInt(1,pcVO.getProdNum()); //상품번호
				pstmt.setString(2,pcVO.getSellerId()); //구매한 아이디
				pstmt.setString(3,pcVO.getUserId()); //판매자 아이디
				pstmt.executeQuery();
				pstmt.close();	        	
	        }

			
	        // interestListVO 테이블 업데이트
	        StringBuilder updateInterestList = new StringBuilder();
	        updateInterestList
	        .append("delete from INTEREST_LIST where PROD_NUM=?");
	        pstmt = con.prepareStatement(updateInterestList.toString());
	        pstmt.setInt(1, pcVO.getProdNum());        
	        pstmt.executeQuery();
	        pstmt.close();
	        
	        // 상품상태 테이블 업데이트
	        StringBuilder prodStatus = new StringBuilder();
	        prodStatus
	        .append("	update PRODUCT_STATUS														")
	        .append("	set PRODUCT_STATUS='판매완료', TRANSACTION_DATE=sysdate	 	")
	        .append("	where PROD_NUM=?																");
	        pstmt = con.prepareStatement(prodStatus.toString());
	        pstmt.setInt(1, pcVO.getProdNum());        
	        pstmt.executeQuery();
	        pstmt.close();
	        
	    } finally {
	        dbCon.dbClose(null,pstmt, con);
	    }
	}//updateTablePrd
	
	
		/**
		 * 상품에 달린 댓글의 리스트를 가져오는 메소드
		 * @param pclVO
		 * @throws SQLException
		 */
		public List<SetCommVO> setCommList(int prodNum)throws SQLException {
		   List<SetCommVO> list= new ArrayList<SetCommVO>();
			Connection con = null;
		    PreparedStatement pstmt = null;
		    ResultSet rs=null;
		    
		    DbConnection dbCon = DbConnection.getInstance();
		    try {
		        con = dbCon.getConn();
		    //쿼리문 생성객체 작성 필요.
		        StringBuilder commList = new StringBuilder();
		        		     		        
		        commList
		        .append("	SELECT COMMENT_NUM,prod_comments,reply_num,reply_comment,write_date,user_id,prod_num													")
		        .append( "   FROM ( SELECT COMMENT_NUM, PROD_COMMENTS, 0 AS REPLY_NUM, '' AS REPLY_COMMENT, WRITE_DATE, USER_ID, PROD_NUM																						")
		        .append("	  FROM PROD_COMMENT											")
		        .append("	 UNION ALL																			")
		        .append("	SELECT COMMENT_NUM, '', REPLY_NUM, REPLY_COMMENT, WRITE_DATE, USER_ID,(													")
		        .append("	SELECT PROD_NUM												")
		        .append("	FROM PROD_COMMENT												")
		        .append("	WHERE COMMENT_NUM = REPLY_COMMENT.COMMENT_NUM) AS PROD_NUM													")
		        .append("	FROM REPLY_COMMENT )												")
		        .append("	where prod_num=?											")
		        .append("	ORDER BY COMMENT_NUM,prod_comments											");

		        
		        pstmt=con.prepareStatement(commList.toString());
		        pstmt.setInt(1, prodNum);
		        
		        rs=pstmt.executeQuery();

		    	//public SetCommVO(String userId, String prodComment, String replyComment, int commNum, int prodNum, int replyNum,
		    	//		Date writeDate)
		        while(rs.next()) {
		        	String userId=rs.getString("user_id");
		        	String prodComment=rs.getString("prod_comments");
		        	String replyComment=rs.getString("reply_comment");
		        	int commNum=rs.getInt("COMMENT_NUM");
		        	int prodNumber=rs.getInt("prod_num");
		        	int replyNum=rs.getInt("reply_num");
		        	Date writeDate=rs.getDate("write_date");
		        	list.add(new SetCommVO(userId, prodComment, replyComment, commNum, prodNumber, replyNum, writeDate));
		        }//end while
		         
		    } finally {
		        dbCon.dbClose(rs,pstmt, con);
		    }//end finally
		    return list;
		}//setCommList
		
}//class
