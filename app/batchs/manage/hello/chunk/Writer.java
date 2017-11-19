package batchs.manage.hello.chunk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class Writer extends AbstractItemWriter {

	@Inject
	private JobContext jobCtx;

	@Inject
	@BatchProperty(name = "outputFile")
	private File outputFile;

	private BufferedWriter writer;

	@Override
	public void open(final Serializable checkpoint) throws Exception {

		final String message = jobCtx.getProperties().getProperty("message");
		System.out.println(message);

		writer = Files.newBufferedWriter(outputFile.toPath(), StandardCharsets.UTF_8);
	}

	@Override
	public void close() throws Exception {

		writer.close();
	}

	@Override
	public void writeItems(final List<Object> items) throws Exception {

		for (final Object obj : items) {

			final String data = (String) obj;
			System.out.println("Writer writeItems : " + data);

			writer.write(data);
			writer.newLine();
		}
	}
}
