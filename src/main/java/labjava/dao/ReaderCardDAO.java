package labjava.dao;

import labjava.model.ReaderCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderCardDAO extends DAO {

    public ReaderCardDAO() {
        super();
    }

    /**
     * Thêm bản ghi thẻ bạn đọc mới
     * @return true nếu insert thành công
     */
    public boolean CreateReaderCard(ReaderCard rc) {
        String sql = "INSERT INTO tbl_reader_card (reader_id, statusCard) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rc.getId());
            ps.setString(2, rc.getStatus());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
