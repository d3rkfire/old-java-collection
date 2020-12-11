package miniGames;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import miniGames.DataList.CULTURE;
import miniGames.DataList.NICKNAME;

public class RandomGirls {
	private JFrame frame;
	private JTextField txtName;
	private JTextField txtAge;
	private JTextField txtBirthdate;
	private JTextField txtSign;
	private JTextArea txtHobbies;
	private JTextArea txtNickname;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RandomGirls window = new RandomGirls();
					window.frame.setVisible(true);
				} catch (Exception e) {}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RandomGirls() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setTitle("Your Date");
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);

		JLabel lblImage = new JLabel("");
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setBounds(314, 11, 300, 400);
		frame.getContentPane().add(lblImage);

		JButton btnGuess = new JButton("Guess");
		btnGuess.setBounds(250, 422, 89, 23);
		frame.getContentPane().add(btnGuess);

		JLabel lblYourCrush = new JLabel("Who will be your date?");
		lblYourCrush.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblYourCrush.setHorizontalAlignment(SwingConstants.CENTER);
		lblYourCrush.setBounds(10, 11, 294, 14);
		frame.getContentPane().add(lblYourCrush);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 66, 65, 17);
		frame.getContentPane().add(lblName);

		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setBounds(85, 63, 160, 20);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);

		txtAge = new JTextField();
		txtAge.setEditable(false);
		txtAge.setColumns(10);
		txtAge.setBounds(85, 94, 160, 20);
		frame.getContentPane().add(txtAge);

		JLabel lblAge = new JLabel("Age:");
		lblAge.setBounds(10, 97, 65, 17);
		frame.getContentPane().add(lblAge);

		txtBirthdate = new JTextField();
		txtBirthdate.setEditable(false);
		txtBirthdate.setColumns(10);
		txtBirthdate.setBounds(85, 125, 160, 20);
		frame.getContentPane().add(txtBirthdate);

		JLabel lblBirthdate = new JLabel("Birthdate:");
		lblBirthdate.setBounds(10, 128, 65, 17);
		frame.getContentPane().add(lblBirthdate);

		txtSign = new JTextField();
		txtSign.setEditable(false);
		txtSign.setColumns(10);
		txtSign.setBounds(85, 156, 160, 20);
		frame.getContentPane().add(txtSign);

		JLabel lblSign = new JLabel("Sign:");
		lblSign.setBounds(10, 159, 65, 17);
		frame.getContentPane().add(lblSign);

		JLabel lblHobbies = new JLabel("Hobbies:");
		lblHobbies.setBounds(10, 190, 65, 17);
		frame.getContentPane().add(lblHobbies);

		txtHobbies = new JTextArea();
		txtHobbies.setEditable(false);
		txtHobbies.setBounds(85, 187, 160, 40);
		txtHobbies.setLineWrap(true);
		txtHobbies.setBackground(null);
		txtHobbies.setBorder(txtName.getBorder());
		frame.getContentPane().add(txtHobbies);

		JLabel lblNickname = new JLabel("Nickname:");
		lblNickname.setBounds(10, 238, 65, 17);
		frame.getContentPane().add(lblNickname);

		txtNickname = new JTextArea();
		txtNickname.setEditable(false);
		txtNickname.setBounds(85, 235, 160, 40);
		txtNickname.setLineWrap(true);
		txtNickname.setBackground(null);
		txtNickname.setBorder(txtName.getBorder());
		frame.getContentPane().add(txtNickname);

		JLabel lblHowYouMeet = new JLabel("How you two will meet:");
		lblHowYouMeet.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblHowYouMeet.setBounds(10, 286, 294, 14);
		frame.getContentPane().add(lblHowYouMeet);

		JLabel lblSituation = new JLabel();
		lblSituation.setVerticalAlignment(SwingConstants.TOP);
		lblSituation.setBounds(10, 311, 235, 100);
		frame.getContentPane().add(lblSituation);

		JLabel lblBiography = new JLabel("Biography:");
		lblBiography.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblBiography.setBounds(10, 41, 294, 14);
		frame.getContentPane().add(lblBiography);

		// Cache map for images
		Map<CULTURE, ArrayList<String>> imgMap = new HashMap<>();
		btnGuess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Random rand = new Random();
				// Get Basic Data
				CULTURE luckyCult = CULTURE.values()[rand.nextInt(CULTURE.values().length)];
				NICKNAME luckyNick = NICKNAME.values()[rand.nextInt(NICKNAME.values().length)];
				// Set Name
				txtName.setText(DataList.getRandomName(luckyCult));
				// Set Birthdate, Age and Sign
				int age = DataList.getRandomAge();
				GregorianCalendar calendar = DataList.getRandomBirthdate(age);
				String zodiacSign = DataList.getZodiacSign(calendar);
				SimpleDateFormat df = new SimpleDateFormat("EEEEEE dd/MMM/yyyy", Locale.ENGLISH);

				txtAge.setText(age + "");
				txtBirthdate.setText(df.format(calendar.getTime()));
				txtSign.setText(zodiacSign);
				// Set Nickname and Hobbies
				txtHobbies.setText(DataList.getRandomHobbies());
				txtNickname.setText(DataList.getRandomNickName(luckyNick));
				// Set Situation
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						lblSituation.setText("<html>" + DataList.getRandomSituation() + "</html>");
					}
				}).start();
				// Set Image
				String mod1 = "", mod2 = "";
				if (!imgMap.containsKey(luckyCult)) {
					String priority = "pinterest";
					mod1 = DataList.getRandomSearchMod1();
					mod2 = DataList.getRandomSearchMod2();
					ArrayList<String> images = DataList.getGoogleImages(priority + " " + mod1 + " " + luckyCult.name() + " " + mod2);
					imgMap.put(luckyCult, images);
				}

				int chosen = rand.nextInt(imgMap.get(luckyCult).size());
				String chosenLink = imgMap.get(luckyCult).get(chosen);
				System.out.println(chosenLink);

				lblImage.setText("Please wait...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							ImageIcon img = new ImageIcon(new URL(chosenLink));
							img.setImage(img.getImage().getScaledInstance(lblImage.getWidth(), lblImage.getWidth() * img.getIconHeight()/img.getIconWidth(), Image.SCALE_SMOOTH));
							lblImage.setIcon(img);
						} catch (IOException e) {e.printStackTrace();}
					}
				}).start();
			}
		});
	}
}

class DataList {
	enum CULTURE {EUROPEAN, ARABIC, CHINESE, KOREAN, JAPANESE}
	static Map<CULTURE, String[]> givenName = getGivenName();
	private static Map<CULTURE, String[]> getGivenName() {
		Map<CULTURE, String[]> gn = new HashMap<>();
		gn.put(CULTURE.EUROPEAN, new String[] {"Emma","Olivia","Ava","Sophia","Isabella","Mia","Charlotte","Abigail","Emily","Harper","Amelia","Evelyn","Elizabeth","Sofia","Madison","Avery","Ella","Scarlett","Grace","Chloe","Victoria","Riley","Aria","Lily","Aubrey","Zoey","Penelope","Lillian","Addison","Layla","Natalie","Camila","Hannah","Brooklyn","Zoe","Nora","Leah","Savannah","Audrey","Claire","Eleanor","Skylar","Ellie","Samantha","Stella","Paisley","Violet","Mila","Allison","Alexa","Anna","Hazel","Aaliyah","Ariana","Lucy","Caroline","Sarah","Genesis","Kennedy","Sadie","Gabriella","Madelyn","Adeline","Maya","Autumn","Aurora","Piper","Hailey","Arianna","Kaylee","Ruby","Serenity","Eva","Naomi","Nevaeh","Alice","Luna","Bella","Quinn","Lydia","Peyton","Melanie","Kylie","Aubree","Mackenzie","Kinsley","Cora","Julia","Taylor","Katherine","Madeline","Gianna","Eliana","Elena","Vivian","Willow","Reagan","Brianna","Clara","Faith","Ashley","Emilia","Isabelle","Annabelle","Rylee","Valentina","Everly","Hadley","Sophie","Alexandra","Natalia","Ivy","Maria","Josephine","Delilah","Bailey","Jade","Ximena","Alexis","Alyssa","Brielle","Jasmine","Liliana","Adalynn","Khloe","Isla","Mary","Andrea","Kayla","Emery","London","Kimberly","Morgan","Lauren","Sydney","Nova","Trinity","Lyla","Margaret","Ariel","Adalyn","Athena","Lilly","Melody","Isabel","Jordyn","Jocelyn","Eden","Paige","Teagan","Valeria","Sara","Norah","Rose","Aliyah","Mckenzie","Molly","Raelynn","Leilani","Valerie","Emerson","Juliana","Nicole","Laila","Makayla","Elise","Mariah","Mya","Arya","Ryleigh","Adaline","Brooke","Rachel","Eliza","Angelina","Amy","Reese","Alina","Cecilia","Londyn","Gracie","Payton","Esther","Alaina","Charlie","Iris","Arabella","Genevieve","Finley","Daisy","Harmony","Anastasia","Kendall","Daniela","Catherine","Adelyn","Vanessa","Brooklynn","Juliette","Julianna","Presley","Summer","Destiny","Amaya","Hayden","Alana","Rebecca","Michelle","Eloise","Lila","Fiona","Callie","Lucia","Angela","Marley","Adriana","Parker","Alexandria","Giselle","Alivia","Alayna","Brynlee","Ana","Harley","Gabrielle","Dakota","Georgia","Juliet","Tessa","Leila","Kate","Jayla","Jessica","Lola","Stephanie","Sienna","Josie","Daleyza","Rowan","Evangeline","Hope","Maggie","Camille","Makenzie","Vivienne","Sawyer","Gemma","Joanna","Noelle","Elliana","Mckenna","Gabriela","Kinley","Rosalie","Brynn","Amiyah","Melissa","Adelaide","Malia","Ayla","Izabella","Delaney","Cali","Journey","Maci","Elaina","Sloane","June","Diana","Blakely","Aniyah","Olive","Jennifer","Paris","Miranda","Lena","Jacqueline","Paislee","Jane","Raegan","Lyric","Lilliana","Adelynn","Lucille","Selena","River","Annie","Cassidy","Jordan","Thea","Mariana","Amina","Miriam","Haven","Remi","Charlee","Blake","Lilah","Ruth","Amara","Kali","Kylee","Arielle","Emersyn","Alessandra","Fatima","Talia","Vera","Nina","Ariah","Allie","Addilyn","Keira","Catalina","Raelyn","Phoebe","Lexi","Zara","Makenna","Ember","Leia","Rylie","Angel","Haley","Madilyn","Kaitlyn","Heaven","Nyla","Amanda","Freya","Journee","Daniella","Danielle","Kenzie","Ariella","Lia","Brinley","Maddison","Shelby","Elsie","Kamila","Camilla","Alison","Ainsley","Ada","Laura","Kendra","Kayleigh","Adrianna","Madeleine","Joy","Juniper","Chelsea","Sage","Erin","Felicity","Gracelyn","Nadia","Skyler","Briella","Aspen","Myla","Heidi","Katie","Zuri","Jenna","Kyla","Kaia","Kira","Sabrina","Gracelynn","Gia","Amira","Alexia","Amber","Cadence","Esmeralda","Katelyn","Scarlet","Kamryn","Alicia","Miracle","Kelsey","Logan","Kiara","Bianca","Kaydence","Alondra","Evelynn","Christina","Lana","Aviana","Dahlia","Dylan","Anaya","Ashlyn","Jada","Kathryn","Jimena","Elle","Gwendolyn","April","Carmen","Mikayla","Annalise","Maeve","Camryn","Helen","Daphne","Braelynn","Carly","Cheyenne","Leslie","Veronica","Nylah","Kennedi","Skye","Evie","Averie","Harlow","Allyson","Carolina","Tatum","Francesca","Aylin","Ashlynn","Sierra","Mckinley","Leighton","Maliyah","Annabella","Megan","Margot","Luciana","Mallory","Millie","Regina","Nia","Rosemary","Saylor","Abby","Briana","Phoenix","Viviana","Alejandra","Frances","Jayleen","Serena","Lorelei","Zariah","Ariyah","Jazmin","Avianna","Carter","Marlee","Eve","Aleah","Remington","Amari","Bethany","Fernanda","Malaysia","Willa","Liana","Ryan","Addyson","Yaretzi","Colette","Macie","Selah","Nayeli","Madelynn","Michaela","Priscilla","Janelle","Samara","Justice","Itzel","Emely","Lennon","Aubrie","Julie","Kyleigh","Sarai","Braelyn","Alani","Lacey","Edith","Elisa","Macy","Marilyn","Baylee","Karina","Raven","Celeste","Adelina","Matilda","Kara","Jamie","Charleigh","Aisha","Kassidy","Hattie","Karen","Sylvia","Winter","Aleena","Angelica","Magnolia","Cataleya","Danna","Henley","Mabel","Kelly","Brylee","Jazlyn","Virginia","Helena","Jillian","Madilynn","Blair","Galilea","Kensley","Wren","Bristol","Emmalyn","Holly","Lauryn","Cameron","Hanna","Meredith","Royalty","Sasha","Lilith","Jazmine","Alayah","Madisyn","Cecelia","Renata","Lainey","Liberty","Brittany","Savanna","Imani","Kyra","Mira","Mariam","Tenley","Aitana","Gloria","Maryam","Giuliana","Skyla","Anne","Johanna","Myra","Charley","Tiffany","Beatrice","Karla","Cynthia","Janiyah","Melany","Alanna","Lilian","Demi","Pearl","Jaylah","Maia","Cassandra","Jolene","Crystal","Everleigh","Maisie","Anahi","Elianna","Hallie","Ivanna","Oakley","Ophelia","Emelia","Mae","Marie","Rebekah","Azalea","Haylee","Bailee","Anika","Monica","Kimber","Sloan","Jayda","Anya","Bridget","Kailey","Julissa","Marissa","Leona","Aileen","Addisyn","Kaliyah","Coraline","Dayana","Kaylie","Celine","Jaliyah","Elaine","Lillie","Melina","Jaelyn","Shiloh","Jemma","Madalyn","Addilynn","Alaia","Mikaela","Adley","Saige","Angie","Dallas","Braylee","Elsa","Emmy","Hayley","Siena","Lorelai","Miah","Royal","Tiana","Elliot","Kori","Greta","Charli","Elliott","Julieta","Alena","Rory","Harlee","Rosa","Ivory","Guadalupe","Jessie","Laurel","Annika","Clarissa","Karsyn","Collins","Kenia","Milani","Alia","Chanel","Dorothy","Armani","Emory","Ellen","Irene","Adele","Jaelynn","Myah","Hadassah","Jayde","Lilyana","Malaya","Kenna","Amelie","Reyna","Teresa","Angelique","Linda","Nathalie","Kora","Zahra","Aurelia","Kalani","Rayna","Jolie","Sutton","Aniya","Jessa","Laylah","Esme","Keyla","Ariya","Elisabeth","Marina","Mara","Meadow","Aliza","Zelda","Lea","Elyse","Monroe","Penny","Lilianna","Lylah","Liv","Scarlette","Kadence","Ansley","Emilee","Perla","Annabel","Alaya","Milena","Karter","Avah","Amirah","Leyla","Livia","Chaya","Wynter","Jaycee","Lailah","Amani","Milana","Lennox","Remy","Zariyah","Clare","Hadlee","Kiera","Rosie","Alma","Kaelyn","Eileen","Jayden","Martha","Noa","Christine","Ariadne","Natasha","Emerie","Tatiana","Joselyn","Joyce","Salma","Amiya","Audrina","Kinslee","Jaylene","Analia","Erika","Lexie","Mina","Patricia","Dulce","Poppy","Aubrielle","Clementine","Lara","Amaris","Milan","Aliana","Kailani","Kaylani","Maleah","Belen","Simone","Whitney","Elora","Claudia","Gwen","Rylan","Antonella","Khaleesi","Arely","Princess","Kenley","Itzayana","Karlee","Paulina","Laney","Bria","Chana","Kynlee","Astrid","Giovanna","Lindsey","Sky","Aryanna","Ayleen","Azariah","Joelle","Nala","Tori","Noemi","Breanna","Emmeline","Mavis","Amalia","Mercy","Tinley","Averi","Aiyana","Alyson","Corinne","Leanna","Madalynn","Briar","Jaylee","Kailyn","Kassandra","Kaylin","Nataly","Amia","Yareli","Cara","Taliyah","Thalia","Carolyn","Estrella","Montserrat","Zaylee","Anabelle","Deborah","Frida","Zaria","Kairi","Katalina","Nola","Erica","Isabela","Jazlynn","Paula","Faye","Louisa","Alessia","Courtney","Reign","Ryann","Stevie","Heavenly","Lisa","Roselyn","Raina","Adrienne","Celia","Estelle","Marianna","Brenda","Kathleen","Paola","Hunter","Ellis","Hana","Lina","Raquel","Aliya","Iliana","Kallie","Emmalynn","Naya","Reina","Wendy","Landry","Barbara","Casey","Karlie","Kiana","Rivka","Kenya","Aya","Carla","Dalary","Jaylynn","Sariah","Andi","Romina","Dana","Danica","Ingrid","Kehlani","Zaniyah","Alannah","Avalynn","Evalyn","Sandra","Veda","Hadleigh","Paityn","Abril","Ciara","Holland","Lillianna","Kai","Bryleigh","Emilie","Carlee","Judith","Kristina","Janessa","Annalee","Zoie","Maliah","Bonnie","Emmaline","Louise","Kaylynn","Monserrat","Nancy","Noor","Vada","Aubriella","Maxine","Nathalia","Tegan","Aranza","Emmie","Brenna","Estella","Ellianna","Kailee","Ailani","Caylee","Zainab","Zendaya","Jana","Julianne","Ellison","Sariyah","Lizbeth","Susan","Alyvia","Jewel","Marjorie","Marleigh","Nathaly","Sharon","Yamileth","Zion","Mariyah","Lyra","Belle","Yasmin","Kaiya","Maren","Marisol","Vienna","Calliope","Hailee","Rayne","Tabitha","Anabella","Blaire","Giana","Milania","Paloma","Amya","Novalee","Harleigh","Ramona","Rhea","Aadhya","Miya","Desiree","Frankie","Sylvie","Jasmin","Moriah","Rosalyn","Kaya","Joslyn","Tinsley","Farrah","Aislinn","Halle","Madyson","Micah","Arden","Bexley","Ari","Aubri","Ayana","Cherish","Davina","Anniston","Riya","Adilynn","Ally","Amayah","Harmoni","Heather","Saoirse","Azaria","Alisha","Nalani","Maylee","Shayla","Briley","Elin","Lilia","Ann","Antonia","Aryana","Chandler","Esperanza","Lilyanna","Alianna","Luz","Meilani"});
		gn.put(CULTURE.ARABIC, new String[] {"Abeer","Ablaa","Aadab","Afaf","Afraa","Afrah","Ahlam","Ahd","Aisha","Alia","Aamaal","Amani","Ameena","Anaan","Anbar","Aneesa","Areebah","Aroob","Asiya","Asalah","Aseelah","Asma","Ayeh","Azeeza","Badriya","Bahiyaa","Banan","Baasima","Baseema","Basheera","Batool","Buthayna","Faiza","Fadwa","Fareeda","Fareeha","Firyal","Fatima","Faatin","Faatina","Fawziya","Ghaada","Ghaaliya","Ghaydaa","Haadiya","Hameeda","Hana","Hanan","Haala","Haleema","Haniya","Hayaam","Haifa","Hind","Huma","Husn","Ibtihaaj","Ikraam","Iman","Inaam","Inaya","Izdihaar","Jameela","Janaan","Jumaana","Kameela","Kawkab","Kawthar","Khadeeja","Khawlah","Khulood","Kouther","Kulthoom","Lamya","Leena","Lubaaba","Lama","Maha","Maisa","Majida","Majeeda","Makaarim","Manaar","Maryam","Mawiya","Maimoona","Maysoon","Mufeeda","Muhja","Muna","Musheera","Nabeela","Nada","Nadia","Nadeeda","Nafeesa","Naeema","Najeeba","Najat","Najwa","Najla","Nashida","Nashita","Nasiha","Nawal","Nawar","Nazaaha","Nazeeya","Nibaal","Naeema","Nesayem","Nida","Nimaat","Nuha","Noor","Nouf","Nusayba","Rabab","Radhiyaa","Raghd","Raaida","Raja","Rana","Rafa","Raniya","Rasha","Rasheeda","Raawiya","Reem","Reema","Ruwayda","Safa","Sahar","Sahla","Sakeena","Saleema","Salma","Salwa","Samaah","Samar","Sameeha","Sameera","Saamiya","Sawda","Shatha","Shaadiya","Shareefa","Sihaam","Suha","Suhayma","Sumaiyaa","Taroob","Thanaa","Tharaa","Thuraya","Tamadhur","Wafeeqa","Wafiya","Wajeeha","Widad","Wisaal","Jasmine","Yasirah","Yafiah","Yakootah","Yamha","Yumn","Zaafira","Zahraa","Zahrah","Zakiyaa","Zainab","Zaina"});
		gn.put(CULTURE.CHINESE, new String[] {"Ai","Baozhai","Biyu","Changchang","Changying","Chen","Chenguang","Chunhua","Chuntao","Cuifen","Daiyu","Dandan","Dongmei","Ehuang","Fan","Fang","Fenfang","Guang","Hong","Hualing","Huan","Huian","Huifang","Huiling","Huiqing","Jia","Jiao","Jiayi","Jiaying","Jie","Jingfei","Jinghua","Jinjing","Ju","Juan","Lan","Lanfen","Lanying","Li","Lifen","Lihua","Lijuan","Liling","Lin","Ling","Liqin","Liqiu","Liu","Luli","Mei","Meifen","Meifeng","Meihui","Meili","Meilin","Meirong","Meixiang","Meixiu","Mingxia","Mingzhu","Ngo-kwang","Ning","Niu","Nüying","O-huang","Peizhi","Qi","Qiang","Qiao","Qiaolian","Qing","Qingge","Qingling","Qingzhao","Qiu","Qiuyue","Rong","Ruolan","Shan","Shu","Shuang","Shuchun","Song","Suyin","Ting","Tung-mei","Wen","Wenling","Wenqian","Xia","Xiang","Xiaodan","Xiaofan","Xiaohui","Xiaoli","Xiaolian","Xiaoling","Xiaoqing","Xifeng","Xingjuan","Xiu","Xiulan","Xiurong","Xiuying","Xue","Ya","Yan","Yanmei","Yanyu","Ying","Yingtai","Yu","Yuan","Yuanjun","Yue","Yun","Zhenzhen","Zhilan","Zhu","Zongying"});
		gn.put(CULTURE.KOREAN, new String[] {"Seo-yun", "Seo-yeon", "Ji-woo", "Ji-yoo", "Ha-yoon", "Seo-hyeon", "Min-seo", "Ha-eun", "Ji-a", "Da-eun", "Ji-Yeon", "Bi-Seol", "Jin-Ha", "Kyung-Hee", "Bo-Yeon", "Jae-Rin", "Ji-Ah", "Seo-Hwa", "Sun-Hye", "Se-Young", "Sun-Bi", "Hwa-Seol", "Ji-Ran", "Hye-Reum", "Yeon-Hae", "Ae-Min", "Shin-Young", "Do-Han", "Si-Rae", "Do-Jin", "Ae-Mi", "Gyeong-Ji", "Na-Jeong", "Si-Wan", "Ri-Hyun", "Hae-Eun", "Min-Young", "So-Yeon-Hwa"});
		gn.put(CULTURE.JAPANESE, new String[] {"Airi", "Akane", "Akari", "Aki", "Anna", "Aoi", "Ayaka", "Ayane", "Ayumi", "Chiaki", "Emi", "Etsuko", "Fuka", "Hana", "Haruka", "Himari", "Hina", "Hinata", "Hitomi", "Hiyori", "Honoka", "Ichika", "Itoe", "Junko", "Kaho", "Kanon", "Kaori", "Karin", "Kazuko", "Koharu", "Kokona", "Kumiko", "Mao", "Mei", "Michiyo", "Mieko", "Miharu", "Mio", "Misaki", "Miwa", "Miyako", "Miyu", "Miyuki", "Momoka", "Nana", "Nanaho", "Nanami", "Naoko", "Natsuki", "Noa", "Reina", "Rie", "Riko", "Rin", "Rina", "Rino", "Rio", "Risa", "Ruka", "Ría", "Sachiko", "Saki", "Sakura", "Sana", "Sara", "Sayuri", "Shiori", "Toshie", "Youko", "Yua", "Yui", "Yuka", "Yume", "Yumi", "Yuna", "Yuzuki"});
		return gn;
	}
	static Map<CULTURE, String[]> familyName = getFamilyName();
	private static Map<CULTURE, String[]> getFamilyName() {
		Map<CULTURE, String[]> fn = new HashMap<>();
		fn.put(CULTURE.EUROPEAN, new String[] {"Anderson", "Ashwoon", "Aikin", "Bateman", "Bongard", "Bowers", "Boyd", "Cannon", "Cast", "Deitz", "Dewalt", "Ebner", "Frick", "Hancock", "Haworth", "Hesch", "Hoffman", "Kassing", "Knutson", "Lawless", "Lawicki", "Mccord", "McCormack", "Miller", "Myers", "Nugent", "Ortiz", "Orwig", "Ory", "Paiser", "Pak", "Pettigrew", "Quinn", "Quizoz", "Ramachandran", "Resnick", "Sagar", "Schickowski", "Schiebel", "Sellon", "Severson", "Shaffer", "Solberg", "Soloman", "Sonderling", "Soukup", "Soulis", "Stahl", "Sweeney", "Tandy", "Trebil", "Trusela", "Trussel", "Turco", "Uddin", "Uflan", "Ulrich", "Upson", "Vader", "Vail", "Valente", "Van Zandt", "Vanderpoel", "Ventotla", "Vogal", "Wagle", "Wagner", "Wakefield", "Weinstein", "Weiss", "Woo", "Yang", "Yates", "Yocum", "Zeaser", "Zeller", "Ziegler", "Bauer", "Baxster", "Casal", "Cataldi", "Caswell", "Celedon", "Chambers", "Chapman", "Christensen", "Darnell", "Davidson", "Davis", "DeLorenzo", "Dinkins", "Doran", "Dugelman", "Dugan", "Duffman", "Eastman", "Ferro", "Ferry", "Fletcher", "Fietzer", "Hylan", "Hydinger", "Illingsworth", "Ingram", "Irwin", "Jagtap", "Jenson", "Johnson", "Johnsen", "Jones", "Jurgenson", "Kalleg", "Kaskel", "Keller", "Leisinger", "LePage", "Lewis", "Linde", "Lulloff", "Maki", "Martin", "McGinnis", "Mills", "Moody", "Moore", "Napier", "Nelson", "Norquist", "Nuttle", "Olson", "Ostrander", "Reamer", "Reardon", "Reyes", "Rice", "Ripka", "Roberts", "Rogers", "Root", "Sandstrom", "Sawyer", "Schlicht", "Schmitt", "Schwager", "Schutz", "Schuster", "Tapia", "Thompson", "Tiernan", "Tisler"});
		fn.put(CULTURE.ARABIC, new String[] {"Syed", "Sheikh", "Mir", "Khondakar", "Akond", "Miah", "Mirza", "Shah", "Munshi", "Dewan", "Gazi", "Kazi", "Khan", "Chowdhury", "Sarkar", "Muhuri", "Malla", "Patoyari", "Molla", "Fakir", "Lashkar", "Hazari", "Shikdar", "Talukdar", "Mazumdar", "Haldar", "Joardar", "Inamdar", "Tarafdar", "Sardar", "Roshandaar", "Chaqladar", "Howladar", "Dihidar", "Bhuiyan", "Mustafi", "Malangi", "Matubbar", "Gomastha", "Panni", "Lohani", "Ansari", "Chisti", "Pir", "Mastan", "Mastana", "Khwaja", "Golandaz", "Niazi", "Pathan", "Prodhan", "Dhali", "Qanungo", "Karkun", "Mallik", "Mandal", "Bishwash", "Pramanik", "Poddar", "Hoque", "Bepari", "Ahmed", "Rahman", "Billah", "Uddin"});
		fn.put(CULTURE.CHINESE, new String[] {"Bai", "Cai", "Cao", "Chang", "Chen", "Cheng", "Cui", "Dai", "Deng", "Ding", "Dong", "Du", "Duan", "Fan", "Fang", "Feng", "Fu", "Gao", "Gong", "Gu", "Guo", "Han", "Hao", "He", "Hou", "Hu", "Huang", "Jia", "Jiang", "Jin", "Kang", "Kong", "Lai", "Li", "Liang", "Lin", "Liu", "Long", "Lu", "Luo", "Ma", "Mao", "Meng", "Pan", "Peng", "Qian", "Qiao", "Qin", "Qiu", "Ren", "Shao", "Shen", "Sheng", "Shi", "Song", "Su", "Sun", "Tan", "Tang", "Tian", "Wan", "Wang", "Wei", "Wen", "Wu", "Xia", "Xiao", "Xie", "Xiong", "Xu", "Xue", "Yan", "Yang", "Yao", "Ye", "Yi", "Yin", "Yu", "Yuan", "Zeng", "Zhang", "Zhao", "Zheng", "Zhong", "Zhou", "Zhu", "Zou"});
		fn.put(CULTURE.KOREAN, new String[] {"Kwon", "Wang", "Goo", "Do", "Kim", "Lee", "Hwang", "Ji", "Yoon", "Jeon", "Moon", "Park", "Jeon", "Lee", "Kwang", "Do", "Hwang", "Park", "Han", "Kim", "Do", "Kwon", "Ha", "Na", "Kang", "Wang", "Go", "So"});
		fn.put(CULTURE.JAPANESE, new String[] {"Sato", "Suzuki", "Takahashi", "Tanaka", "Watanabe", "Ito", "Yamamoto", "Nakamura", "Kobayashi", "Kato", "Yoshida", "Yamada", "Sasaki", "Yamaguchi", "Saito", "Matsumoto", "Inoue", "Kimura", "Hayashi", "Shimizu", "Yamazaki", "Mori", "Abe", "Ikeda", "Hashimoto", "Yamashita", "Ishikawa", "Nakajima", "Maeda", "Fujita", "Ogawa", "Goto", "Okada", "Hasegawa", "Murakami", "Kondo", "Ishii", "Saito (different kanji)", "Sakamoto", "Endo", "Aoki", "Fujii", "Nishimura", "Fukuda", "Ota", "Miura", "Fujiwara", "Okamoto", "Matsuda", "Nakagawa", "Nakano", "Harada", "Ono", "Tamura", "Takeuchi", "Kaneko", "Wada", "Nakayama", "Ishida", "Ueda", "Morita", "Hara", "Shibata", "Sakai", "Kudo", "Yokoyama", "Miyazaki", "Miyamoto", "Uchida", "Takagi", "Ando", "Taniguchi", "Ohno", "Maruyama", "Imai", "Takada", "Fujimoto", "Takeda", "Murata", "Ueno", "Sugiyama", "Masuda", "Sugawara", "Hirano", "Kojima", "Otsuka", "Chiba", "Kubo", "Matsui", "Iwasaki", "Sakurai", "Kinoshita", "Noguchi", "Matsuo", "Nomura", "Kikuchi", "Sano", "Onishi", "Sugimoto", "Arai"});
		return fn;
	}
	static String getRandomName(CULTURE cult) {
		String name = "";
		Random rand = new Random();
		String gn = givenName.get(cult)[rand.nextInt(givenName.get(cult).length)];
		String fn = familyName.get(cult)[rand.nextInt(familyName.get(cult).length)];

		// Given then Family: EUROPEAN, ARABIC (MUSLIM)
		// Family then Given: CHINESE, KOREAN, JAPANESE
		// Arabic (Muslim) family name requires "Al-" (clan, family). Eg: Al-Assad
		if (cult == CULTURE.EUROPEAN) name = gn + " " + fn;
		else if (cult == CULTURE.ARABIC) name = gn + " Al-" + fn;
		else name = fn + " " + gn;

		return name;
	}


	enum NICKNAME {AWESOME, YUMMY, FEMININE, NATURE, BESTFRIEND, CHARACTERISTIC, CUTE, CARTOON, PET, HILARIOUS}
	static Map<NICKNAME, String[]> nickName = getNickName();
	private static Map<NICKNAME, String[]> getNickName() {
		Map<NICKNAME, String[]> nn = new HashMap<>();
		nn.put(NICKNAME.AWESOME, new String[] {"Amiga", "Hermana", "Bebe", "Amour", "Amore", "Coco", "FrouFrou", "Amor", "Cherie", "Chica", "Sierra", "Fifille", "Senorita", "Rumba", "Amorcita", "Frauline", "Pinata", "Princesa", "Frau Frau", "Fiesta", "Lucera", "Chiquita", "Voila", "Florecita", "Chiquitita", "Maus", "Dolce", "Ami", "Shnookie", "Dulce"});
		nn.put(NICKNAME.YUMMY, new String[] {"Dumpling", "Carrot", "Red Velvet", "Gumdrop", "Baby Carrot", "Poppy", "Juicy", "Sweet Pepper", "Poppyseed", "Cinnamon", "Hot Pepper", "Double Bubble", "Cookie", "Hot Sauce", "Bubble Gum", "Cupcake", "Cholula", "Coollata", "Sugar", "Pop Tart", "Sweet Tea", "Pina colada", "Red Hot", "Tea Time", "Limon", "Dots", "Dunkin Donut", "Taco", "Dottie", "Donut", "French Fry", "Twixie", "Crumbles", "Honeybun", "Trixie", "Sweet ‘n Sour", "Biscuit", "Trix", "Strawberry", "Pepper", "Apple Jack", "Shortcake", "Cinnamon", "Cheerio", "Tiramisu", "Ginger", "Golden Graham", "Gingersnap", "Cajun", "Goldie Pie", "Hershey’s Kiss", "Muffin", "Pickle Pie", "Nik-L-Nip", "Tart", "Sweet Pickle", "Pop Rocks", "Lovey Pie", "Pickle Dear", "Taffy", "Snickerdoodle", "Ritzie", "Smartie", "Raisin", "Marshmallow Mama", "Pixie", "Chippie", "Spice", "Candy Joy", "Tater Tot", "Apple", "Candy", "Tater", "Cherry", "Peep", "Sweet Tater", "Plum", "Tootsie Roll", "Taters ‘N Beans", "Plummy Pie", "Peppermint", "Tato", "Dearie Pie", "Butterfinger", "Radish", "Honey Pie", "Gummi Bear", "Parsnip", "Brown Sugar Baby", "Kit-Kat", "Roccoli", "Pie Baby", "Torta", "Veggie", "Baby Cakes", "Bean"});
		nn.put(NICKNAME.FEMININE, new String[] {"Beautiful", "Beauty", "Pretty Pie", "Belle", "Cutie Pie", "Pretty Dish", "Dolly", "Cutie Mama", "Gracey", "Foxy Lady", "Cutie Cakes", "Cher", "Foxy Mama", "Cutie Coo", "Sweet Doll", "Cutie", "Pretty Mama", "Dreamey", "Doll", "Pretty Fox", "Lovely Lady", "Bonny Lass", "Pretty Lady", "Queenie"});
		nn.put(NICKNAME.NATURE, new String[] {"Firefly", "Ladybug", "Loblolly", "Flower", "Bug", "Maple", "Sunshine", "Cricket", "Ash", "Diamond", "Fly", "River", "Mouse", "Beetle", "Brook", "Little Bear", "Inchworm", "Butternut", "Pearl", "Roly Poly", "Magnolia", "Supermouse", "Butterfly", "Holly", "Little Doe", "Dragonfly", "Scarlet", "Quail", "Skimmer", "Pecan", "Ducky", "Tadpole", "Persimmon", "Kitten", "Froggy", "Sassafras", "Cat", "Lacewing", "Tupelo", "Rabbit", "Bumblebee", "Raindrop", "Kitty", "Queen Bee", "Cloudy", "Otter", "Hopper", "Sunny", "Lamby", "Strider", "Springy", "Duckling", "Katydid", "Winter", "Birdy", "Treehopper", "Autumn", "Sparrow", "Bud", "Cloud", "Robin", "Blossom", "Rainbow", "Chickie", "Petal", "Sprinkle", "Baby Bird", "Leaf", "Mist", "Little Bird", "Roots", "Moon", "Hedgehog", "Seed", "New Moon", "Meercat", "Sprout", "Venus", "Prairiedog", "Violet", "Amethyst", "Piglet", "Daffodil", "Tiger Eye", "Monkey", "Pansy", "Amber", "Baby Seal", "Tulip", "Aura", "Bunny", "Rosie", "Cats Eye", "Bunny Rabbit", "Twiggy", "Citrine", "Chinchilla", "Twig", "Jade", "Penguin", "Willow", "Jet", "Panda", "Aspen", "Opal", "Koala", "Rosebud", "Sapphire", "Firefly", "Sugar Maple", "Serpentine", "Star", "Sweetgum", "Topaz"});
		nn.put(NICKNAME.BESTFRIEND, new String[] {"Bestie", "Westie", "Friend of Life", "BFF", "Troof", "F4L (Friend for Life)", "BFFL", "GF (Great Friend)", "Friend-a", "Biffle", "BF2", "Friend-o", "Bitch", "Superfriend", "Fringa", "B.F.E. (Best Friend Ever)", "Zuperfriend", "Wonderfriend", "Forever Amiga", "Uberfriend", "Empire State Friend", "Forever Friend", "Friendest", "Friendtinga", "Best Best", "UF (Ultimate Friend)", "Heartie", "Wiffle", "F2TE (Friend to the End )", "Soul Friend"});
		nn.put(NICKNAME.CHARACTERISTIC, new String[] {"Gams", "Giggles", "Shortie", "Bubble Butt", "Speedy", "Squirt", "Cheeky", "Genius", "Half Pint", "Blondie", "Curie", "Tiny", "Prego (for friends who are expecting)", "Happy", "Tyke", "Sassy", "Grumpy", "Midge", "Shorty", "Doc", "Lil Girl", "Dimples", "Skinny Minny", "Teeny", "Pixie Stick", "Thin Thing", "Stretch", "Bossy", "Fatty", "Big Bird", "Chuckles", "Fattykins", "Mama Long Legs", "Smirk", "Fattykitten", "Headlights", "Smiley", "Bass Player", "Cardigan", "The Look", "Short Shorts", "Paisley", "Fury", "Hoodie", "Plaid", "Freckles", "Mini Skirt", "Sweet Feet", "Tickles", "Mini Mini", "Pinkie", "Honey Locks", "Mini Freckles", "Freckletini", "Dimpling", "Ms. Dimples", "Dimpleguss", "Rapunzel", "Twinkly", "Ms. Congeniality", "The Vest", "Bellbottom", "High Water"});
		nn.put(NICKNAME.CUTE, new String[] {"Boo", "Boo Two", "Dilly Wallop", "Lala", "Two Boo", "Bootsie", "Lulu", "T-Bo", "Smookie", "NiNi", "La-T", "Smoochie", "Pookie", "Timi", "Schnookums", "Toots", "Tootie", "Sweetums", "Mimi", "Pookie Bear", "Filly Fally", "Titi", "Pookie Bug", "Silly Sally", "Tata", "Pookie Tookie", "Gilly", "Boo Boo", "Tookie Wookie", "Tilly", "Boo Bear", "Tutu", "TeeToe", "Boo Bug", "Toto", "Sweet Toe", "Boo Thing", "Dilly Dally", "FleeBee"});
		nn.put(NICKNAME.CARTOON, new String[] {"Bubbles", "Bambi", "Luna", "Buttercup", "Babs Bunny", "Cindy Lou Who", "Princess", "Pebbles", "Iris", "Snow White", "Winnie", "Princess Daisy", "Mini Me", "Tweety", "Angelica", "Munchkin", "Kirby", "Daria", "Oompa Loompa", "Vixey", "Eliza", "Thumper", "Yoshi", "Merida", "Stitch", "Fluttershy", "Betty Boop", "Unikitty", "Applebloom", "Peppa Pig", "Dot", "Catbug", "Sleeping Beauty"});
		nn.put(NICKNAME.PET, new String[] {"Lovey", "Wifey", "Heart ‘O Mine", "Lover", "Tootsie", "Dovey", "Darling", "Babe", "Poppet", "Dear", "Baby", "Dearey", "Dearest", "Sweetheart", "Sweets"});
		nn.put(NICKNAME.HILARIOUS, new String[] {"Tika", "Till Toe", "Giggle-loo", "Gummy Pop", "Wiggles", "TigTig", "Dortie Doo", "Quacky", "Gillygum", "Lily Mama", "Itchy", "Gillygut", "Dilly Mama", "Quinta", "Sillygilly", "SweetFox", "Quillsby", "Goosey-kins", "Rockette", "Bellsby", "Tigglekins", "Ittie", "Bellasky", "Tigerini", "Fiddle", "Trellasky", "Bellatini", "Puff Puff", "Cinderells", "Pretty-kins", "Cotton", "Beatuski", "Sweetie-kins", "Snuggles", "Pretty-ums", "Pebblybutt", "Huggie", "Tiggy-ums", "Gooseybutt", "Cuddle Pig", "Figgy", "Ticklebutt", "Tricksy", "Briggsi", "Knucklebutt", "Tisket", "Whitski", "Figgle", "Tasket", "Wiggleski", "Tiggle"});
		return nn;
	}
	static String getRandomNickName(NICKNAME nick) {
		String names = "";
		Random rand = new Random();

		// Primary nickname
		names += nickName.get(nick)[rand.nextInt(nickName.get(nick).length)] + "; ";
		// Secondary nicknames from other characteristics
		int count = rand.nextInt(4)+1;
		for (int i = 1; i<count; i++) {
			NICKNAME randomNick = NICKNAME.values()[rand.nextInt(NICKNAME.values().length)];
			String name = nickName.get(randomNick)[rand.nextInt(nickName.get(randomNick).length)];
			if (names.contains(name)) i--;
			else names += name + "; ";
		}

		return names;
	}

	static String[] hobbies = new String[] {"Cooking", "Photography", "Writing", "Gardening", "Knitting", "Birdwatching", "Sewing", "Drawing", "Hiking", "Dancing", "Chess", "Reading", "Scrapbooking", "Video Games", "Geocaching", "Cycling", "Running", "Shopping", "Origami", "Kirigami", "Painting", "Baking", "Roller Skating", "Embroidery", "Calligraphy", "Backpacking", "Singing", "Reading", "Blogging", "Jewelry-making", "Candle-making", "Video-making", "Public-speaking", "Lap-dancing", "Belly-dancing", "Pole-dancing", "Food Styling", "Volunteering", "Writing", "Flower Arrangement", "Gym", "Swimming", "Yoga"};
	static String getRandomHobbies() {
		String hobbies = "";
		Random rand = new Random();

		// Primary hobby
		hobbies += DataList.hobbies[rand.nextInt(DataList.hobbies.length)] + "; ";
		// Secondary hobbies from other characteristics
		int count = rand.nextInt(4) + 1;
		for (int i = 1; i<count; i++) {
			String tmp = DataList.hobbies[rand.nextInt(DataList.hobbies.length)];
			if (hobbies.contains(tmp)) i--;
			else hobbies += tmp + "; ";
		}
		return hobbies;
	}

	static int getRandomAge() {
		int age = new Random().nextInt(10) + 16;
		return age;
	}

	static GregorianCalendar getRandomBirthdate(int age) {
		GregorianCalendar cal = new GregorianCalendar();

		// for month 0 = jan. date and year are normal.
		// Eg: 2 years old.
		// if born before and on today 2 years ago, then 2 years old.
		// if born after today 3 years ago, then 2 years old.

		int extra = randBetween(0, 1);
		int year = randBetween(cal.get(Calendar.YEAR) - age - extra, cal.get(Calendar.YEAR) - age);
		cal.set(Calendar.YEAR, year);

		int dayOfYear = 0;
		if (extra == 1) dayOfYear = randBetween(cal.get(Calendar.DAY_OF_YEAR) + 1, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
		else dayOfYear = randBetween(1, cal.get(Calendar.DAY_OF_YEAR));
		cal.set(Calendar.DAY_OF_YEAR, dayOfYear);

		return cal;
	}
	private static int randBetween(int start, int end) {
		return start + (int)Math.round(Math.random() * (end - start));
	}

	static String getZodiacSign(GregorianCalendar cal) {
		String sign = "";
		int date = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;

		if (month == 1) {
			if (date <= 19) sign = "Capricorn";
			else sign = "Aquarius";
		} else if (month == 2) {
			if (date <= 18) sign = "Aquarius";
			else sign = "Pisces";
		} else if (month == 3) {
			if (date <= 20) sign = "Pisces";
			else sign = "Aries";
		} else if (month == 4) {
			if (date <= 19) sign = "Aries";
			else sign = "Taurus";
		} else if (month == 5) {
			if (date <= 20) sign = "Taurus";
			else sign = "Gemini";
		} else if (month == 6) {
			if (date <= 20) sign = "Gemini";
			else sign = "Cancer";
		} else if (month == 7) {
			if (date <= 22) sign = "Cancer";
			else sign = "Leo";
		} else if (month == 8) {
			if (date <= 22) sign = "Leo";
			else sign = "Virgo";
		} else if (month == 9) {
			if (date <= 22) sign = "Virgo";
			else sign = "Libra";
		} else if (month == 10) {
			if (date <= 22) sign = "Libra";
			else sign = "Scorpio";
		} else if (month == 11) {
			if (date <= 21) sign = "Scorpio";
			else sign = "Sagittarius";
		} else if (month == 12) {
			if (date <= 21) sign = "Sagittarius";
			else sign = "Capricorn";
		}

		return sign;
	}

	private enum WEATHER {NEUTRAL, DAY, NIGHT};
	static String getRandomSituation() {
		Map<WEATHER, String[]> mapWeather = new HashMap<>();
		mapWeather.put(WEATHER.NEUTRAL, new String[] {"windless", "breezy", "clear", "windy", "stormy", "cloudy", "rainy","gloomy", "dull", "fine", "lovely", "beautiful", "pleasant"});
		mapWeather.put(WEATHER.DAY, new String[] {"sunny", "hot", "bright"});
		mapWeather.put(WEATHER.NIGHT, new String[] {"cold", "freezing", "chilly", "icy"});

		String[] arrPartsOfDay = new String[] {"morning", "day", "evening", "night", "midnight"};
		String[] arrMonths = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		String[] arrP1 = new String[] {"after", "right after"};
		String[] arrV1 = new String[] {"take a walk outside", "leave home", "go out for a walk", "go out for a meal"};
		String[] arrFutures = new String[] {"are going to", "will"};
		String[] arrV2 = new String[] {"meet", "run into", "bump into", "come across", "chance upon", "encounter", "stumble upon"};
		String[] arrAdjs = new String[] {"sexy", "hot", "cute", "beautiful", "lovely", "cheerful", "passionate", "witty"};
		String[] arrGirls = new String[] {"girl", "young lady", "young woman", "miss"};
		String[] arrP2 = new String[] {"behind", "in front of", "beside", "by", "near"};
		String[] arrP2O = new String[] {"a library", "your school", "the corridor", "a small tree", "the railway", "a cinema", "a restaurant", "a theme park", "the city hall", "a quiet garden", "a hidden walkway in the park"};
		String[] arrEndingWords = new String[] {"love at first sight", "the first time you two meet", "as if you two are fated together", "a delightful coincidence", "a pleasant surprise"};
		// Generate date
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_YEAR, randBetween(0, cal.getActualMaximum(Calendar.DAY_OF_YEAR)));
		String year = "";
		if (cal.before(Calendar.getInstance())) {
			year = " next year";
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		}
		String month = arrMonths[cal.get(Calendar.MONTH)] + " ";

		int chosenDay = cal.get(Calendar.DAY_OF_MONTH);
		String dayOfMonth = chosenDay + "";
		int chosenDayLastDigit = chosenDay % 10;
		if (chosenDayLastDigit == 1 && chosenDay != 11) dayOfMonth += "st";
		else if (chosenDayLastDigit == 2 && chosenDay != 12) dayOfMonth += "nd";
		else if (chosenDayLastDigit == 3 && chosenDay != 13) dayOfMonth += "rd";
		else dayOfMonth += "th";
		// Generate 1st chant
		String partOfDay = arrPartsOfDay[randBetween(0, arrPartsOfDay.length - 1)];
		String season = "";
		if (randBetween(0, 5) == 0) {
			int chosenMonth = cal.get(Calendar.MONTH);
			if (chosenMonth < 2) season = "winter ";
			if (chosenMonth < 5) season = "spring ";
			if (chosenMonth < 8) season = "summer ";
			if (chosenMonth < 11) season = "autumn ";
			else season = "winter ";
		}
		WEATHER chosenWeather;
		if (partOfDay.contains("morning") || partOfDay.contains("day")) chosenWeather = WEATHER.DAY;
		else chosenWeather = WEATHER.NIGHT;
		if (season.contains("winter")) chosenWeather = WEATHER.NIGHT;

		String weather = "";
		if (randBetween(0, 1) == 0) weather = mapWeather.get(WEATHER.NEUTRAL)[randBetween(0, mapWeather.get(WEATHER.NEUTRAL).length - 1)];
		else weather = mapWeather.get(chosenWeather)[randBetween(0, mapWeather.get(chosenWeather).length - 1)];
		weather += " ";
		// Generate 2nd chant
		String prep1 = arrP1[randBetween(0, arrP1.length -1 )];
		String verb1 = arrV1[randBetween(0, arrV1.length -1 )];
		// Generate 3rd chant
		String future = arrFutures[randBetween(0, arrFutures.length -1 )];
		String verb2 = arrV2[randBetween(0, arrV2.length -1 )];
		String adj = arrAdjs[randBetween(0, arrAdjs.length -1 )];
		String girl = arrGirls[randBetween(0, arrGirls.length -1 )];
		String prep2 = "", prep2o = "";
		if (verb2.contains("meet") || verb2.contains("run into") || randBetween(0, 10) == 0) {
			prep2 = " " + arrP2[randBetween(0, arrP2.length -1 )] + " ";
			prep2o = arrP2O[randBetween(0, arrP2O.length -1 )];
		}
		// Generate Ending Word
		String endingWord = arrEndingWords[randBetween(0, arrEndingWords.length - 1)];
		
		String situation = String.format("On a %s%s%s of %s%s%s, %s you %s, you %s %s this %s %s%s%s. It is %s.",
				weather, season, partOfDay,
				month, dayOfMonth, year,
				prep1, verb1,
				future, verb2, adj, girl, prep2, prep2o,
				endingWord);
		return situation;
	}

	static String getRandomSearchMod1() {
		String[] mods = {"sexy", "hot", "cute", "beautiful", "lovely", "single"};
		return mods[new Random().nextInt(mods.length)];
	}

	static String getRandomSearchMod2() {
		String[] mods = {"girl", "lady", "woman", "chick", "babe", "beauty"};
		return mods[new Random().nextInt(mods.length)];
	}

	static ArrayList<String> getGoogleImages(String query) {
		ArrayList<String> result = new ArrayList<>();
		try {
			String encodedQuery = URLEncoder.encode(query, "UTF-8");
			String exclusion = "-ugly -male -art -anime -old -\"middle-age\"";
			query += " " + exclusion;
			Document document = Jsoup.connect("https://www.google.com.kh/search?tbm=isch&tbs=ic:color,iar:t,itp:face&q=" + encodedQuery)
					.userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36")
					.get();
			// Old Method (No longer works)
//			Elements images = document.select("div.rg_meta.notranslate");
//			for(int i = 0; i< images.size(); i++) {
//				String json = images.get(i).text();
//				int start = json.indexOf("\"ou\":\"");
//				json = json.substring(start + 6);
//				int end = json.indexOf("\"");
//				json = json.substring(0, end);
//				result.add(json);
//			}
			
			// Updated (2020)
			Elements scripts = document.select("script[nonce]");
			for (int i = scripts.size()-1; i >= 0; i--) {
				String scriptContent = scripts.get(i).html();
				if (scriptContent.contains(".jpg")) {
					// Get image links
					String pattern = "(?<=\")" + "http.+.jpg" + "(?!=\")";
					Matcher matcher = Pattern.compile(pattern).matcher(scriptContent);
					while (matcher.find()) result.add(matcher.group());
					break;
				}
			}
		} catch (IOException e) {e.printStackTrace();}
		return result;
	}
}