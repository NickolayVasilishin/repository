/**
  * Created by Nikolay_Vasilishin on 9/15/2016.
  */

import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.{ServerSocket, Socket}
import java.util.Scanner
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{ExecutorService, Executors}

import scala.util.Random

object TextToSocketProducer {
  var stop: AtomicBoolean = new AtomicBoolean(false)


  val text =
    """"Quite so! You have not observed. And yet you have seen. That is just my point. Now, I know that there are seventeen steps, because I have both seen and observed. By the way, since you are interested in these little problems, and since you are good enough to chronicle one or two of my trifling experiences, you may be interested in this." He threw over a sheet of thick, pink-tinted notepaper which had been lying open upon the table. "It came by the last post," said he. "Read it aloud."
      |
      |The note was undated, and without either signature or address.
      |
      |"There will call upon you to-night, at a quarter to eight o'clock," it said, "a gentleman who desires to consult you upon a matter of the very deepest moment. Your recent services to one of the royal houses of Europe have shown that you are one who may safely be trusted with matters which are of an importance which can hardly be exaggerated. This account of you we have from all quarters received. Be in your chamber then at that hour, and do not take it amiss if your visitor wear a mask."
      |
      |"This is indeed a mystery," I remarked. "What do you imagine that it means?"
      |
      |"I have no data yet. It is a capital mistake to theorise before one has data. Insensibly one begins to twist facts to suit theories, instead of theories to suit facts. But the note itself. What do you deduce from it?"
      |
      |I carefully examined the writing, and the paper upon which it was written.
      |
      |"The man who wrote it was presumably well to do," I remarked, endeavouring to imitate my companion's processes. "Such paper could not be bought under half a crown a packet. It is peculiarly strong and stiff."
      |
      |"Peculiar--that is the very word," said Holmes. "It is not an English paper at all. Hold it up to the light."
      |
      |I did so, and saw a large "E" with a small "g," a "P," and a large "G" with a small "t" woven into the texture of the paper.
      |
      |"What do you make of that?" asked Holmes.
      |
      |"The name of the maker, no doubt; or his monogram, rather."
      |
      |"Not at all. The 'G' with the small 't' stands for 'Gesellschaft,' which is the German for 'Company.' It is a customary contraction like our 'Co.' 'P,' of course, stands for 'Papier.' Now for the 'Eg.' Let us glance at our Continental Gazetteer." He took down a heavy brown volume from his shelves. "Eglow, Eglonitz--here we are, Egria. It is in a German-speaking country--in Bohemia, not far from Carlsbad. 'Remarkable as being the scene of the death of Wallenstein, and for its numerous glass-factories and paper-mills.' Ha, ha, my boy, what do you make of that?" His eyes sparkled, and he sent up a great blue triumphant cloud from his cigarette.
      |
      |"The paper was made in Bohemia," I said.
      |
      |"Precisely. And the man who wrote the note is a German. Do you note the peculiar construction of the sentence--'This account of you we have from all quarters received.' A Frenchman or Russian could not have written that. It is the German who is so uncourteous to his verbs. It only remains, therefore, to discover what is wanted by this German who writes upon Bohemian paper and prefers wearing a mask to showing his face. And here he comes, if I am not mistaken, to resolve all our doubts."
      |
      |As he spoke there was the sharp sound of horses' hoofs and grating wheels against the curb, followed by a sharp pull at the bell. Holmes whistled.
      |
      |"A pair, by the sound," said he. "Yes," he continued, glancing out of the window. "A nice little brougham and a pair of beauties. A hundred and fifty guineas apiece. There's money in this case, Watson, if there is nothing else."
      |
      |"I think that I had better go, Holmes."
      |
      |"Not a bit, Doctor. Stay where you are. I am lost without my Boswell. And this promises to be interesting. It would be a pity to miss it."
      |
      |"But your client--"
      |
      |"Never mind him. I may want your help, and so may he. Here he comes. Sit down in that armchair, Doctor, and give us your best attention."
      |
      |A slow and heavy step, which had been heard upon the stairs and in the passage, paused immediately outside the door. Then there was a loud and authoritative tap.
      |
      |"Come in!" said Holmes.
      |
      |A man entered who could hardly have been less than six feet six inches in height, with the chest and limbs of a Hercules. His dress was rich with a richness which would, in England, be looked upon as akin to bad taste. Heavy bands of astrakhan were slashed across the sleeves and fronts of his double-breasted coat, while the deep blue cloak which was thrown over his shoulders was lined with flame-coloured silk and secured at the neck with a brooch which consisted of a single flaming beryl. Boots which extended halfway up his calves, and which were trimmed at the tops with rich brown fur, completed the impression of barbaric opulence which was suggested by his whole appearance. He carried a broad-brimmed hat in his hand, while he wore across the upper part of his face, extending down past the cheekbones, a black vizard mask, which he had apparently adjusted that very moment, for his hand was still raised to it as he entered. From the lower part of the face he appeared to be a man of strong character, with a thick, hanging lip, and a long, straight chin suggestive of resolution pushed to the length of obstinacy.
      |
      |"You had my note?" he asked with a deep harsh voice and a strongly marked German accent. "I told you that I would call." He looked from one to the other of us, as if uncertain which to address.
      |
      |"Pray take a seat," said Holmes. "This is my friend and colleague, Dr. Watson, who is occasionally good enough to help me in my cases. Whom have I the honour to address?"
      |
      |"You may address me as the Count Von Kramm, a Bohemian nobleman. I understand that this gentleman, your friend, is a man of honour and discretion, whom I may trust with a matter of the most extreme importance. If not, I should much prefer to communicate with you alone."
      |
      |I rose to go, but Holmes caught me by the wrist and pushed me back into my chair. "It is both, or none," said he. "You may say before this gentleman anything which you may say to me."
      |
      |The Count shrugged his broad shoulders. "Then I must begin," said he, "by binding you both to absolute secrecy for two years; at the end of that time the matter will be of no importance. At present it is not too much to say that it is of such weight it may have an influence upon European history."
      |
      |"I promise," said Holmes.
      |
      |"And I."
      |
      |"You will excuse this mask," continued our strange visitor. "The august person who employs me wishes his agent to be unknown to you, and I may confess at once that the title by which I have just called myself is not exactly my own."
      |
      |"I was aware of it," said Holmes dryly.
      |
      |"The circumstances are of great delicacy, and every precaution has to be taken to quench what might grow to be an immense scandal and seriously compromise one of the reigning families of Europe. To speak plainly, the matter implicates the great House of Ormstein, hereditary kings of Bohemia."
      |
      |"I was also aware of that," murmured Holmes, settling himself down in his armchair and closing his eyes.
      |
      |Our visitor glanced with some apparent surprise at the languid, lounging figure of the man who had been no doubt depicted to him as the most incisive reasoner and most energetic agent in Europe. Holmes slowly reopened his eyes and looked impatiently at his gigantic client.
      |
      |"If your Majesty would condescend to state your case," he remarked, "I should be better able to advise you."
      |
      |The man sprang from his chair and paced up and down the room in uncontrollable agitation. Then, with a gesture of desperation, he tore the mask from his face and hurled it upon the ground. "You are right," he cried; "I am the King. Why should I attempt to conceal it?"
      |
      |"Why, indeed?" murmured Holmes. "Your Majesty had not spoken before I was aware that I was addressing Wilhelm Gottsreich Sigismond von Ormstein, Grand Duke of Cassel-Felstein, and hereditary King of Bohemia."
      |
      |"But you can understand," said our strange visitor, sitting down once more and passing his hand over his high white forehead, "you can understand that I am not accustomed to doing such business in my own person. Yet the matter was so delicate that I could not confide it to an agent without putting myself in his power. I have come incognito from Prague for the purpose of consulting you."
      |
      |"Then, pray consult," said Holmes, shutting his eyes once more.
      |
      |"The facts are briefly these: Some five years ago, during a lengthy visit to Warsaw, I made the acquaintance of the well-known adventuress, Irene Adler. The name is no doubt familiar to you."
      |
      |"Kindly look her up in my index, Doctor," murmured Holmes without opening his eyes. For many years he had adopted a system of docketing all paragraphs concerning men and things, so that it was difficult to name a subject or a person on which he could not at once furnish information. In this case I found her biography sandwiched in between that of a Hebrew rabbi and that of a staff-commander who had written a monograph upon the deep-sea fishes.
      |
      |"Let me see!" said Holmes. "Hum! Born in New Jersey in the year 1858. Contralto--hum! La Scala, hum! Prima donna Imperial Opera of Warsaw--yes! Retired from operatic stage--ha! Living in London--quite so! Your Majesty, as I understand, became entangled with this young person, wrote her some compromising letters, and is now desirous of getting those letters back."
      |
      | At three o'clock precisely I was at Baker Street, but Holmes had not yet returned. The landlady informed me that he had left the house shortly after eight o'clock in the morning. I sat down beside the fire, however, with the intention of awaiting him, however long he might be. I was already deeply interested in his inquiry, for, though it was surrounded by none of the grim and strange features which were associated with the two crimes which I have already recorded, still, the nature of the case and the exalted station of his client gave it a character of its own. Indeed, apart from the nature of the investigation which my friend had on hand, there was something in his masterly grasp of a situation, and his keen, incisive reasoning, which made it a pleasure to me to study his system of work, and to follow the quick, subtle methods by which he disentangled the most inextricable mysteries. So accustomed was I to his invariable success that the very possibility of his failing had ceased to enter into my head.
      |
      |It was close upon four before the door opened, and a drunken-looking groom, ill-kempt and side-whiskered, with an inflamed face and disreputable clothes, walked into the room. Accustomed as I was to my friend's amazing powers in the use of disguises, I had to look three times before I was certain that it was indeed he. With a nod he vanished into the bedroom, whence he emerged in five minutes tweed-suited and respectable, as of old. Putting his hands into his pockets, he stretched out his legs in front of the fire and laughed heartily for some minutes.
      |
      |"Well, really!" he cried, and then he choked and laughed again until he was obliged to lie back, limp and helpless, in the chair.
      |
      |"What is it?"
      |
      |"It's quite too funny. I am sure you could never guess how I employed my morning, or what I ended by doing."
      |
      |"I can't imagine. I suppose that you have been watching the habits, and perhaps the house, of Miss Irene Adler."
      |
      |"Quite so; but the sequel was rather unusual. I will tell you, however. I left the house a little after eight o'clock this morning in the character of a groom out of work. There is a wonderful sympathy and freemasonry among horsey men. Be one of them, and you will know all that there is to know. I soon found Briony Lodge. It is a bijou villa, with a garden at the back, but built out in front right up to the road, two stories. Chubb lock to the door. Large sitting-room on the right side, well furnished, with long windows almost to the floor, and those preposterous English window fasteners which a child could open. Behind there was nothing remarkable, save that the passage window could be reached from the top of the coach-house. I walked round it and examined it closely from every point of view, but without noting anything else of interest.
      |
      |"I then lounged down the street and found, as I expected, that there was a mews in a lane which runs down by one wall of the garden. I lent the ostlers a hand in rubbing down their horses, and received in exchange twopence, a glass of half-and-half, two fills of shag tobacco, and as much information as I could desire about Miss Adler, to say nothing of half a dozen other people in the neighbourhood in whom I was not in the least interested, but whose biographies I was compelled to listen to."
      |
      |"And what of Irene Adler?" I asked.
      |
      |"Oh, she has turned all the men's heads down in that part. She is the daintiest thing under a bonnet on this planet. So say the Serpentine-mews, to a man. She lives quietly, sings at concerts, drives out at five every day, and returns at seven sharp for dinner. Seldom goes out at other times, except when she sings. Has only one male visitor, but a good deal of him. He is dark, handsome, and dashing, never calls less than once a day, and often twice. He is a Mr. Godfrey Norton, of the Inner Temple. See the advantages of a cabman as a confidant. They had driven him home a dozen times from Serpentine-mews, and knew all about him. When I had listened to all they had to tell, I began to walk up and down near Briony Lodge once more, and to think over my plan of campaign.
      |
      |"This Godfrey Norton was evidently an important factor in the matter. He was a lawyer. That sounded ominous. What was the relation between them, and what the object of his repeated visits? Was she his client, his friend, or his mistress? If the former, she had probably transferred the photograph to his keeping. If the latter, it was less likely. On the issue of this question depended whether I should continue my work at Briony Lodge, or turn my attention to the gentleman's chambers in the Temple. It was a delicate point, and it widened the field of my inquiry. I fear that I bore you with these details, but I have to let you see my little difficulties, if you are to understand the situation."
      |
      |"I am following you closely," I answered.
      |
      |"I was still balancing the matter in my mind when a hansom cab drove up to Briony Lodge, and a gentleman sprang out. He was a remarkably handsome man, dark, aquiline, and moustached--evidently the man of whom I had heard. He appeared to be in a great hurry, shouted to the cabman to wait, and brushed past the maid who opened the door with the air of a man who was thoroughly at home.
      |
      |"He was in the house about half an hour, and I could catch glimpses of him in the windows of the sitting-room, pacing up and down, talking excitedly, and waving his arms. Of her I could see nothing. Presently he emerged, looking even more flurried than before. As he stepped up to the cab, he pulled a gold watch from his pocket and looked at it earnestly, 'Drive like the devil,' he shouted, 'first to Gross & Hankey's in Regent Street, and then to the Church of St. Monica in the Edgeware Road. Half a guinea if you do it in twenty minutes!'
      |
      |"Away they went, and I was just wondering whether I should not do well to follow them when up the lane came a neat little landau, the coachman with his coat only half-buttoned, and his tie under his ear, while all the tags of his harness were sticking out of the buckles. It hadn't pulled up before she shot out of the hall door and into it. I only caught a glimpse of her at the moment, but she was a lovely woman, with a face that a man might die for.
      |
      |" 'The Church of St. Monica, John,' she cried, 'and half a sovereign if you reach it in twenty minutes.'
      |
      |"This was quite too good to lose, Watson. I was just balancing whether I should run for it, or whether I should perch behind her landau when a cab came through the street. The driver looked twice at such a shabby fare, but I jumped in before he could object. 'The Church of St. Monica,' said I, 'and half a sovereign if you reach it in twenty minutes.' It was twenty-five minutes to twelve, and of course it was clear enough what was in the wind.
      |
      |"My cabby drove fast. I don't think I ever drove faster, but the others were there before us. The cab and the landau with their steaming horses were in front of the door when I arrived. I paid the man and hurried into the church. There was not a soul there save the two whom I had followed and a surpliced clergyman, who seemed to be expostulating with them. They were all three standing in a knot in front of the altar. I lounged up the side aisle like any other idler who has dropped into a church. Suddenly, to my surprise, the three at the altar faced round to me, and Godfrey Norton came running as hard as he could towards me.
      |
      |" 'Thank God,' he cried. 'You'll do. Come! Come!'
      |
      |" 'What then?' I asked.
      |
      |" 'Come, man, come, only three minutes, or it won't be legal.'
      |
      |"I was half-dragged up to the altar, and before I knew where I was I found myself mumbling responses which were whispered in my ear, and vouching for things of which I knew nothing, and generally assisting in the secure tying up of Irene Adler, spinster, to Godfrey Norton, bachelor. It was all done in an instant, and there was the gentleman thanking me on the one side and the lady on the other, while the clergyman beamed on me in front. It was the most preposterous position in which I ever found myself in my life, and it was the thought of it that started me laughing just now. It seems that there had been some informality about their license, that the clergyman absolutely refused to marry them without a witness of some sort, and that my lucky appearance saved the bridegroom from having to sally out into the streets in search of a best man. The bride gave me a sovereign, and I mean to wear it on my watch chain in memory of the occasion."
      |
      |"This is a very unexpected turn of affairs," said I; "and what then?"
      |
      |"Well, I found my plans very seriously menaced. It looked as if the pair might take an immediate departure, and so necessitate very prompt and energetic measures on my part. At the church door, however, they separated, he driving back to the Temple, and she to her own house. 'I shall drive out in the park at five as usual,' she said as she left him. I heard no more. They drove away in different directions, and I went off to make my own arrangements."
      | """.replaceAll("\"", "")
      .replaceAll("\\.", "")
      .replaceAll("\\!", "")
      .replaceAll(",", "")
      .replaceAll("\\?", "")
      .replaceAll("\\'", "")
      .replaceAll("--", "")
      .replaceAll("\\|", "")
      .split(" ")
      .filter(_.trim != "")
      .map(_.toLowerCase)

  def produceInBackground() = {
    val socket = new ServerSocket(10000)
    Executors.newSingleThreadExecutor().execute(new Runnable {
      override def run(): Unit = {
        val s = socket.accept()
        val out = new BufferedWriter(new
            OutputStreamWriter(s.getOutputStream))
        while (true) {
          out.write(text(Random.nextInt(text.length)) + " ")
          if (Random.nextBoolean() && Random.nextBoolean()) {
            out.write("\n")
            out.flush()
          }
          Thread.sleep(500)
        }
      }
    })


    //    Executors.newSingleThreadExecutor().execute(new Runnable {
    //      override def run(): Unit = consume()
    //    })
  }

  def consume() = {
    val s = new Socket("localhost", 10000)
    val stream = s.getInputStream
    println("Working")
    while (!stop.get()) {
      val scanner = new Scanner(stream)
      println(scanner.nextLine())
    }
  }
}
