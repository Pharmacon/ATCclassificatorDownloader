package by.bsuir.pharmacon.classifier.atc;

public class testEnMain {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("********ATC classification downloader******");
		new RecursiveWalker().walkATCClassification();
		
	}

}
