package myai;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Service {

	public static void main(String[] args) {

		Service service = new Service();

		List<double[]> images = service.getImageList();
		List<double[]> teaches = service.getTeachList();

		images.forEach(image->{for(double v : image)System.out.print(v+",");System.out.println();});
		teaches.forEach(teach->{for(double v : teach)System.out.print(v+",");System.out.println();});

//		service.register(images, teaches);

	}

	public void register(double[] image, double[] teach) {

		String img = "" + image[0];
		for ( int j=1; j<image.length; j++ ) {
			img += "," + image[j];
		}
		String t = "" + teach[0];
		for ( int j=1; j<teach.length; j++ ) {
			t += "," + teach[j];
		}
		upsert(img, t);

	}

	private void upsert(String image, String teach) {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		String sql = "REPLACE INTO data VALUES( ?, ? )";

		try ( Connection connection = DriverManager.getConnection("jdbc:sqlite:nndb.db");
				PreparedStatement ps = connection.prepareStatement(sql); ) {

			ps.setString(1, image);
			ps.setString(2, teach);

			ps.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public List<double[]> getImageList() {
		return selectImageArray().stream().map(strs -> {
			String[] strArray = strs.split(",");
			double[] array = new double[strArray.length];
			for ( int i=0; i<strArray.length; i++ ) {
				array[i] = Double.parseDouble(strArray[i]);
			}
			return array;
		}).collect(Collectors.toList());
	}

	public List<double[]> getTeachList() {
		return selectTeachArray().stream().map(strs -> {
			String[] strArray = strs.split(",");
			double[] array = new double[strArray.length];
			for ( int i=0; i<strArray.length; i++ ) {
				array[i] = Double.parseDouble(strArray[i]);
			}
			return array;
		}).collect(Collectors.toList());
	}

	private List<String> selectImageArray() {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		List<String> list = new ArrayList<>();

		String sql = "SELECT * FROM data ORDER BY image";

		try ( Connection connection = DriverManager.getConnection("jdbc:sqlite:nndb.db");
				PreparedStatement ps = connection.prepareStatement(sql); ) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String image = rs.getString("image");
				list.add(image);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return list;

	}

	private List<String> selectTeachArray() {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		List<String> list = new ArrayList<>();

		String sql = "SELECT * FROM data ORDER BY image";

		try ( Connection connection = DriverManager.getConnection("jdbc:sqlite:nndb.db");
				PreparedStatement ps = connection.prepareStatement(sql); ) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String teach = rs.getString("teach");
				list.add(teach);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return list;

	}

}



