package com.example.abasteceaqui.tools;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalvarNoBancoDeDadosTask extends AsyncTask<Void, Void, Void> {
    private final String nomePosto;
    private final String enderecoPosto;
    private final double latitude;
    private final double longitude;

    public SalvarNoBancoDeDadosTask(String nomePosto, String enderecoPosto, double latitude, double longitude) {
        this.nomePosto = nomePosto;
        this.enderecoPosto = enderecoPosto;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Connection connection = null;
        try {
            // Carrega a classe JDBC para PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Cria a conexão com o banco de dados
            String jdbcUrl = "jdbc:postgresql://motty.db.elephantsql.com:5432/pxkwtvhx";
            String username = "pxkwtvhx";
            String password = "ntKLofOzuo3q38PH8uKHzzeLzIOBaSVH";
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Insira o endereço na tabela addresses
            String sqlInsertAddress = "INSERT INTO addresses (address, latitude, longitude) VALUES (?, ?, ?) RETURNING addressid";
            PreparedStatement preparedStatementAddress = connection.prepareStatement(sqlInsertAddress);
            preparedStatementAddress.setString(1, enderecoPosto);
            preparedStatementAddress.setDouble(2, latitude);
            preparedStatementAddress.setDouble(3, longitude);
            ResultSet result = preparedStatementAddress.executeQuery();
            int addressId = -1;
            if (result.next()) {
                addressId = result.getInt("addressid");
            }
            preparedStatementAddress.close();

            // Insira o nome do posto na tabela fuelstation com a referência ao endereço
            String sqlInsertFuelStation = "INSERT INTO fuelstation (stationname, addressid) VALUES (?, ?)";
            PreparedStatement preparedStatementFuelStation = connection.prepareStatement(sqlInsertFuelStation);
            preparedStatementFuelStation.setString(1, nomePosto);
            preparedStatementFuelStation.setInt(2, addressId);
            preparedStatementFuelStation.executeUpdate();
            preparedStatementFuelStation.close();



            // Fecha a conexão
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Qualquer ação que você deseja executar após concluir as operações de rede
    }
}
