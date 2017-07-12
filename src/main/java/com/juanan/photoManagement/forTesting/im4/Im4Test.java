package com.juanan.photoManagement.forTesting.im4;

import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

public class Im4Test {

	public static void main(String... argv) throws IOException, InterruptedException, IM4JavaException {
		String imPath="C:\\Program Files\\ImageMagick-7.0.6-Q16";
		ConvertCmd cmd = new ConvertCmd();
		cmd.setSearchPath(imPath);

		IMOperation op = new IMOperation();
		op.addImage("d:\\sofiaJuanan.jpg");
		op.resize(800,600);
		op.addImage("d:\\sofiaJuanan_resized.jpg");

		cmd.run(op);		
	}
}
