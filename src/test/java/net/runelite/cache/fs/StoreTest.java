package net.runelite.cache.fs;

import java.io.IOException;
import java.util.Random;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class StoreTest
{
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	@BeforeClass
	public static void before()
	{
		System.setProperty("java.io.tmpdir", "d:/rs/07/temp/");
	}
	
	@Test
	public void testOneFile() throws IOException
	{
		try (Store store = new Store(folder.getRoot()))
		{
			Index index = store.addIndex(0);
			Archive archive = index.addArchive(0);
			File file = archive.addFile(0);
			file.setContents("test".getBytes());

			store.save();
		
			try (Store store2 = new Store(folder.getRoot()))
			{
				store2.load();
				
				Assert.assertEquals(store, store2);
			}
		}
	}
	
	private static final int NUMBER_OF_FILES = 1024;
	
	@Test
	public void testManyFiles() throws IOException
	{
		Random random = new Random(42L);

		try (Store store = new Store(folder.getRoot()))
		{
			Index index = store.addIndex(0);
			Archive archive = index.addArchive(0);
			for (int i = 0; i < NUMBER_OF_FILES; ++i)
			{
				File file = archive.addFile(i);
			//	file.setNameHash(random.nextInt());
				byte[] data = new byte[random.nextInt(1024)];
				random.nextBytes(data);
				file.setContents(data);
			}
			
			store.save();
			
			try (Store store2 = new Store(folder.getRoot()))
			{
				store2.load();
				
				Assert.assertEquals(store, store2);
			}
		}
	}
}
