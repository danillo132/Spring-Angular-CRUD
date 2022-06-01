package curso.api.rest.service;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioService implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private JdbcTemplate template;
	
	
	
	public byte[] gerarRelatorio(String nomeRelatorio, Map<String, Object> params, ServletContext servletContext) throws Exception {
		
		
		//Obter conexão com o banco de dados
		
		Connection connection = template.getDataSource().getConnection();
		
		
		//Carregar caminho do relatorio jasper
		
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator + nomeRelatorio +
				".jasper";
		
		//GErar o relatorio com dados e conexao
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper, params, connection);
		
		//Exporta byte para pdf para fazer o download
		
		byte[] retorno = JasperExportManager.exportReportToPdf(print);
		connection.close();
		
		return retorno;
		
		
	}
}
