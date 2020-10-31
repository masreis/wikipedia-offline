package net.marcoreis.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TesteException {

    private static Logger logger = LoggerFactory.getLogger(TesteException.class);

    public void teste() {
	try {
	    algumMetodo();
	} catch (Exception e) {
	    logger.error("metodo falhou", e);
	}
    }

    private void algumMetodo() {
	// TODO Auto-generated method stub
	
    }

}
