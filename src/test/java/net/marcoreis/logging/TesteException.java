package net.marcoreis.logging;

import org.apache.log4j.Logger;

public class TesteException {

    private static Logger logger = Logger.getLogger(TesteException.class);

    public void teste() {
	try {
	    algumMetodo();
	} catch (Exception e) {
	    logger.error("metodo falhou", e);
	}
    }

}
