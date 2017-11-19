package batchs.manage.hello.chunk;

import java.io.BufferedReader;
import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Reader extends AbstractItemReader {

	@Inject
	private JobContext jobCtx;

	@Inject
	@BatchProperty(name = "inputFile")
	private File inputFile;

	private BufferedReader reader;

	@Override
	public void open(Serializable checkpoint) throws Exception {

		final String message = jobCtx.getProperties().getProperty("message");
		System.out.println(message);

		reader = Files.newBufferedReader(inputFile.toPath(), StandardCharsets.UTF_8);
	}

	@Override
	public void close() throws Exception {

		reader.close();
	}

	@Override
	public Object readItem() throws Exception {

		final String data = reader.readLine();
		System.out.println("Reader readItem : " + data);
		return data;
	}
}
