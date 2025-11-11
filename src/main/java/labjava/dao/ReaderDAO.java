package labjava.dao;
import labjava.model.Member;
import labjava.model.Address;
import labjava.model.Librarian;
import labjava.model.Name;
import labjava.model.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ReaderDAO kế thừa DAO để dùng chung Connection con
 */
public class ReaderDAO extends DAO {

    public ReaderDAO() {
        super(); // mở kết nối từ lớp cha DAO
    }


    public boolean insert(Reader r) {
        String sqlReader = "INSERT INTO tbl_reader() VALUES()"; // chỉ tạo id (auto_increment)
        String sqlMember = "INSERT INTO tbl_member(username, password_hash, email, birth_date, phone, reader_id) VALUES(?,?,?,?,?,?)";

        try {

            con.setAutoCommit(false);

            int readerId;
            try (PreparedStatement psReader = con.prepareStatement(sqlReader, Statement.RETURN_GENERATED_KEYS)) {
                psReader.executeUpdate();
                try (ResultSet rs = psReader.getGeneratedKeys()) {
                    if (rs.next()) {
                        readerId = rs.getInt(1);
                    } else {
                        con.rollback();
                        throw new SQLException("Không lấy được reader_id sau khi insert vào tbl_reader");
                    }
                }
            }


            try (PreparedStatement psMember = con.prepareStatement(sqlMember)) {
                psMember.setString(1, r.getUsername());
                psMember.setString(3, r.getEmail());
                psMember.setString(4, r.getBirthDate());
                psMember.setString(5, r.getPhone());
                psMember.setInt(6, readerId);

                int rows = psMember.executeUpdate();
                if (rows == 0) {
                    con.rollback();
                    return false;
                }
            }
            con.commit();
            return true;

        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignored) {}
            e.printStackTrace();
            return false;
        } finally {
            try { con.setAutoCommit(true); } catch (SQLException ignored) {}
         }
    }


    // Trả về Reader hoặc Librarian (đều extends Member)
    public Reader findByLogin(String username, String passHash) {
        String sql = "SELECT id, username, email, phone, birth_date,reader_id,librarian_id FROM tbl_member WHERE username=? AND password_hash=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passHash); // nếu dùng BCrypt: đổi sang SELECT theo username rồi check BCrypt trong Java
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reader r = new Reader();
                    r.setId(rs.getInt("reader_id"));
                    r.setUsername(rs.getString("username"));
                    r.setEmail(rs.getString("email"));
                    r.setPhone(rs.getString("phone"));
                    r.setBirthDate(rs.getString("birth_date"));
                    System.out.println(r.getPhone());
                    return r;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Reader getFullInfoReader(int readerId) {
        System.out.println("start");
        String sql = """
        SELECT 
            m.username AS m_username, m.email AS m_email, m.phone AS m_phone, m.birth_date AS m_birth_date,
            n.id AS n_id, n.given_name AS n_given_name, n.surname AS n_surname, n.middle_name AS n_middle_name,
            a.id AS a_id, a.house_number AS a_house_number, a.building AS a_building, a.street_number AS a_street_number,
            a.district AS a_district, a.ward AS a_ward, a.province AS a_province, a.description AS a_description,
            r.id AS r_id
        FROM tbl_member m
        JOIN tbl_reader r      ON r.id = m.reader_id
        LEFT JOIN tbl_name n    ON n.id = m.name_id
        LEFT JOIN tbl_address a ON a.id = m.address_id
        WHERE r.id = ?
        LIMIT 1
    """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                // Pack Name
                Name name = null;
                Integer nId = (Integer) rs.getObject("n_id");
                if (nId != null) {
                    name = new Name();
                    name.setId(nId);
                    name.setGivenName(rs.getString("n_given_name"));
                    name.setSurname(rs.getString("n_surname"));
                    name.setMiddleName(rs.getString("n_middle_name"));
                }

                // Pack Address
                Address addr = null;
                Integer aId = (Integer) rs.getObject("a_id");
                if (aId != null) {
                    addr = new Address();
                    addr.setId(aId);
                    addr.setHouseNumber(rs.getString("a_house_number"));
                    addr.setBuilding(rs.getString("a_building"));
                    addr.setStreetNumber(rs.getString("a_street_number"));
                    addr.setDistrict(rs.getString("a_district"));
                    addr.setWard(rs.getString("a_ward"));
                    addr.setProvince(rs.getString("a_province"));
                    addr.setDescription(rs.getString("a_description"));
                }

                // Map Reader
                Reader r = new Reader();
                r.setId(rs.getInt("r_id"));                  // ✅ Reader.id = tbl_reader.id
                r.setUsername(rs.getString("m_username"));
                r.setEmail(rs.getString("m_email"));
                r.setPhone(rs.getString("m_phone"));
                java.sql.Date bd = rs.getDate("m_birth_date");
                r.setBirthDate(bd != null ? bd.toString() : null);
                r.setName(name);
                r.setAddress(addr);
                System.out.println(r.getPhone());
                System.out.println(r.getName().getGivenName());
                return r;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
// ... (các import khác

    // ... (Câu query BASE_READER_INFO_QUERY giữ nguyên)
    public List<Map<String, Object>> getAllReader() {

        // Bước 1: Lấy danh sách các đối tượng Reader CƠ BẢN
        List<Reader> readerList = getBasicReaders();

        // Đây là danh sách Map chúng ta sẽ trả về
        List<Map<String, Object>> resultList = new ArrayList<>();

        // Bước 2: Lặp qua danh sách Reader
        for (Reader reader : readerList) {

            // Bước 3: Lấy thông tin bổ sung
            int count = getBorrowedCountForReader(reader.getId());
            double debt = getFineDebtForReader(reader.getId());

            // Bước 4: Chuyển đổi (Reader + Thông tin phụ) -> Map
            Map<String, Object> map = convertReaderToMap(reader, count, debt);
            resultList.add(map);
        }

        return resultList;
    }

    /**
     * Hàm 2 (Public): Tìm kiếm bạn đọc (Đúng thiết kế)
     * Vẫn trả về List<Map> để không ảnh hưởng Servlet/JSP.
     */
    public List<Map<String, Object>> searchReader(String readerCode) {

        // Bước 1: Lấy danh sách đối tượng Reader CƠ BẢN (đã lọc)
        List<Reader> readerList = getBasicReadersByCode(readerCode);

        List<Map<String, Object>> resultList = new ArrayList<>();

        // Bước 2, 3, 4: Lặp, lấy chi tiết, và chuyển đổi
        for (Reader reader : readerList) {
            int count = getBorrowedCountForReader(reader.getId());
            double debt = getFineDebtForReader(reader.getId());

            Map<String, Object> map = convertReaderToMap(reader, count, debt);
            resultList.add(map);
        }

        return resultList;
    }


    // ===================================================================
    // HÀM PHỤ (PRIVATE) ĐỂ TÁCH NHỎ LOGIC
    // ===================================================================

    /**
     * [Phụ 1] Lấy danh sách ĐỐI TƯỢNG Reader cơ bản
     */
    private List<Reader> getBasicReaders() {
        List<Reader> list = new ArrayList<>();
        String sql = "SELECT r.id, m.username, n.surname, n.middle_name, n.given_name " +
                "FROM tbl_reader r " +
                "JOIN tbl_member m ON r.id = m.reader_id " +
                "LEFT JOIN tbl_name n ON m.name_id = n.id";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToBasicReader(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * [Phụ 2] Lấy danh sách ĐỐI TƯỢNG Reader cơ bản (Lọc theo Code)
     */
    private List<Reader> getBasicReadersByCode(String readerCode) {
        List<Reader> list = new ArrayList<>();
        String sql = "SELECT r.id, m.username, n.surname, n.middle_name, n.given_name " +
                "FROM tbl_reader r " +
                "JOIN tbl_member m ON r.id = m.reader_id " +
                "LEFT JOIN tbl_name n ON m.name_id = n.id " +
                "WHERE m.username LIKE ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + readerCode + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBasicReader(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * [Phụ 3] Helper: Map 1 hàng RS sang 1 đối tượng Reader (cơ bản)
     */
    private Reader mapResultSetToBasicReader(ResultSet rs) throws SQLException {
        Reader r = new Reader();
        r.setId(rs.getInt("id"));
        r.setUsername(rs.getString("username"));

        Name name = new Name();
        name.setSurname(rs.getString("surname"));
        name.setMiddleName(rs.getString("middle_name"));
        name.setGivenName(rs.getString("given_name"));

        if (name.getSurname() != null || name.getGivenName() != null) {
            r.setName(name);
        }
        return r;
    }

    /**
     * [Phụ 4] Helper: Chuyển đổi Reader -> Map (cho JSP)
     */
    private Map<String, Object> convertReaderToMap(Reader reader, int count, double debt) {
        Map<String, Object> map = new HashMap<>();

        map.put("readerId", reader.getId());
        map.put("readerCode", reader.getUsername());

        // Giả sử Reader có hàm getFullName() như tôi đã đề xuất
        // Nếu không, bạn phải lấy từ reader.getName()...
        map.put("fullName", reader.getFullName());

        // Thêm dữ liệu tính toán
        map.put("borrowedCount", count);
        map.put("fineDebt", debt);

        return map;
    }

    /**
     * [Phụ 5] Lấy số sách đang mượn (Giữ nguyên)
     */
    private int getBorrowedCountForReader(int readerId) {
        String sql = "SELECT COUNT(db.id) " +
                "FROM tbl_document_borrow db " +
                "JOIN tbl_borrow_slip bs ON db.borrow_slip_id = bs.id " +
                "WHERE bs.reader_id = ? AND bs.status = 'borrowing'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { return rs.getInt(1); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /**
     * [Phụ 6] Lấy nợ phạt (tính động) (Giữ nguyên)
     */
    private double getFineDebtForReader(int readerId) {
        String sql = "SELECT COALESCE(SUM( DATEDIFF(CURDATE(), db.due_at) * fine.amount ), 0) " +
                "FROM tbl_document_borrow db " +
                "JOIN tbl_borrow_slip bs ON db.borrow_slip_id = bs.id " +
                "CROSS JOIN tbl_fine fine " +
                "WHERE bs.reader_id = ? " +
                "  AND bs.status = 'borrowing' " +
                "  AND db.due_at < CURDATE() " +
                "  AND fine.fine_type = 'Late return'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, readerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { return rs.getDouble(1); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}

