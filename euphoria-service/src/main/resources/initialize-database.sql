-- Initializes database and tables.
-- To run: `cat initialize-database.sql | mysql -u root -p`

DROP DATABASE IF EXISTS euphoria;

DROP USER IF EXISTS "euphoria"@"localhost";

CREATE DATABASE euphoria;

USE euphoria;

SET GLOBAL time_zone = "+0:00";

CREATE USER "euphoria"@"localhost" IDENTIFIED WITH mysql_native_password BY "euphoria";

GRANT ALL PRIVILEGES ON euphoria.* TO "euphoria"@"localhost";

-- educationLevel is one of: "NOHIGHSCHOOL",
--                           "HIGHSCHOOL",
--                           "GED",
--                           "SOMECOLLEGE",
--                           "ASSOCIATES",
--                           "BACHELORS",
--                           "MASTERS",
--                           "PHD",
--                           "MD",
--                           "JD"
CREATE TABLE users (userId INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(40) NOT NULL,
                    email VARCHAR (40) NOT NULL,
                    phoneNumber VARCHAR(20) NOT NULL,
                    educationLevel VARCHAR(20) NOT NULL,
                    description TEXT NOT NULL,
                    dateCreated DATETIME NOT NULL); 

INSERT INTO users
    (name, email, phoneNumber, educationLevel, description, dateCreated)
VALUES
    ("Johnny Appleseed", "john@appleseed.com", "123-456-7890", "JD", "I like Macintoshes.", "2017-03-06 15:20:00"),
    ("Tim Apple", "tim@apple.com", "456-123-7890", "MASTERS", "I also like Macintoshes.", "2018-02-10 18:45:00"),
    ("Jeff Bozo", "jeff@bozo.com", "890-123-4567", "BACHELORS", "I will not work in Queens.", "2019-08-01 01:00:00");

CREATE TABLE companies (companyId INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(40) NOT NULL,
                        website VARCHAR (40) NOT NULL,
                        description TEXT NOT NULL,
                        dateCreated DATETIME NOT NULL);

INSERT INTO companies
    (name, website, description, dateCreated)
VALUES
    ("Apple", "apple.com", "Macintoshes.", "2018-02-11 18:50:00"),
    ("Amazon", "bozo.com", "Not in Queens.", "2019-08-02 01:15:00");

-- location is one of "NEWYORK",
--                    "LONDON",
--                    "HONGKONG",
--                    "BERLIN",
--                    "BEIJING",
--                    "WASHINGTON"
-- industry is one of "EDUCATION",
--                    "ENERGY",
--                    "FINANCE",
--                    "FOOD",
--                    "HEALTHCARE",
--                    "INSURANCE",
--                    "MEDIA",
--                    "RETAIL",
--                    "SERVICES",
--                    "TECHNOLOGY",
--                    "TRANSPORT",
--                    "UTILITIES"
-- skillLevel is one of "INTERNSHIP",
--                      "ENTRYLEVEL",
--                      "ASSOCIATE",
--                      "SENIOR",
--                      "DIRECTOR",
--                      "EXECUTIVE"
CREATE TABLE postings (postingId INT AUTO_INCREMENT PRIMARY KEY,
                       companyId INT NOT NULL,  -- FOREIGN KEY(companyId) REFERENCES companies (companyId),
                       jobTitle VARCHAR (30) NOT NULL,
                       description TEXT NOT NULL,
                       location VARCHAR(20) NOT NULL,
                       industry VARCHAR(20) NOT NULL,
                       skillLevel VARCHAR(20) NOT NULL,
                       dateCreated DATETIME NOT NULL);

INSERT INTO postings
    (companyId, jobTitle, description, location, industry, skillLevel, dateCreated)
VALUES
    (123, "Underwater Basket Weaver", "Must lift.", "WASHINGTON", "SERVICES", "INTERNSHIP", "2018-07-10 02:30:00"),
    (456, "Frontend Developer", "Must know everything about React.js.", "NEWYORK", "TECHNOLOGY", "EXECUTIVE", "2019-02-12 12:00:00"),
    (789, "Backend Developer", "Must know nothing about React.js.", "NEWYORK", "TECHNOLOGY", "INTERNSHIP", "2019-03-16 23:59:59");

CREATE TABLE applications (applicationId INT AUTO_INCREMENT PRIMARY KEY,
                           postingId INT NOT NULL,  -- FOREIGN KEY(postingId), REFERENCES postings (postingId),
                           userId INT NOT NULL, -- FOREIGN KEY(userId), REFERENCES users (userID), 
                           resume LONGBLOB NOT NULL,
                           coverLetter LONGBLOB NOT NULL,
                           dateCreated DATETIME NOT NULL);

INSERT INTO applications
    (postingId, userId, resume, coverLetter, dateCreated)
VALUES
	(1, 1, x'255044462d312e340a25e2e3cfd30a352030206f626a0a3c3c2f46696c7465722f466c6174654465636f64652f4c656e67746820323134383e3e73747265616d0a789ced596d8fdb3612feae5f41a45f5220a64551af41716d93bb06091a5c9b6e5114cda1d0cab4ad462f8e24efdef6d7f4a77686a2244af65ab2e50039e01238ab597386f3f2cc33a4f2d178716398c40b2c72b332fe7563fc687c344c6a72cf21f78645dec0977f18cc246f8ddffe63929561fbc4b36d921a8e6bcba7443ed93e35f119bed61eebefb7c62f46260dbb9c38a64dc1768acfdc35a92b2dd8415f50ab1a5d46f06fb1b9c8c05a6eddfaad79a83bde85f398bbf8d3a9d78292d50987ee524c9e09c9b32079bf31ea13930644e6cfa436082e7c7cf2ee159ae5bed4871fa9b4ac760539317e9ae13b7ac45d4679703a593216b5f0820894cb68c06475a2c07525ce8be078ad7b9125bdc2742a17976210c7152aa101241d42a79ffbd13638bb2a4360cd2c4b97eb91484ed4a84b8cc750c70e302ff533f73cead79930a581818c6b0f38e172336bad53eaa0067e1f16488bb90be4427473cba7410d3562b916f57d55f69ea0561da3c2b30dcca2426d3799241099ab25a9930f9d3ebb0b41dfb135dcd6f27c42b4e007d83b9932198e5a7879eba101cb698944893309f168c57b9125c3f2745a17576310ca158aa161243d02a07e05465be2fc613580d7cce2f4323e1ecfe97a0df991b3a0365b8b966f52c63b2e1bca6af9232c79b9319d2bdb4807911c16ae9f88d98c697146993a0f30d7a18c2944f404b5ea18639e6d6016636abbc93c71446397a6563c74f9ec0e055b9ea701ba96e7f325e6c5774f274c46a3165ede9368c00e5a9251e24cbe3c5aef5e64c9a0389dd2c5c5184472855a6810490fe1d3cfff683b9c3fc906e09a591a3ddfa3d19cacd5902a2d8b499bb5c47c8b72b723b3a15caf7e84282f35a5d36413e22086c382f532309b2419e7ed9d0ff89c37a0d09fd59a631479a6fa2c82d4f69229e256ef52aac9872e9fdd95a06f320dc5b53c9f227d86bd76225f32967ad5e54d08e63cbbe5945a9a498ec72aad87940ccbd2e95c5c857e1857a881068df4086e7ab91feb82f367d60053f3cad2cbf66830272b35644666f1da682d061e7559475f03512d7e84192f35a533631b633f888372f5137039339a04ff42082f6e8ce5778c783408c8cd1a42c3df33e2ba48c5ae6552077e9f1a4f1925e4cb9b3ff085e86915c6a8e94a156baa8a89c0420d3e51c3092c6ad52af654158fe3cc4715a753b9eeeb9bcfe496f4391c3f3e8b4e9fd7141681bb9ea323c946d9b3e47608a45771b57c11572ff6d1075191975b1156e556884ac72358e147acb8be471df788957f179b308bff0cab38cf4ee31a827201db5c1af93649c87d5e7c88b30db92dc22cda8a9294db7c9facc8ad2059988a151171b515c55fa7cd72d3a5b6a399ce4735028ef9ec344898addacda533645de4e9489f3ac01c8e1e1325af44268a30491e483fa54355cb0cb0273ad53594625f88e569356edad4d2b49a382dee638910e338439b654efdcab4136dbcc3748b07a2df1c071f735aad6c765f89322ae25d15df8905d66bc477e6502fd0438effdb84cb2d8f725b0ba029501341274b9fb5f543792c8876e96551d8be24545dfd4e24f96e1cf98e8721a0cacd563c3468db867782e4190086919d28ca3c6bbb021e01fd291d370d75f19934fd439147a22cc93a2f1a3bcf4feb7bbe3e34df861f04092b9288b0ace0db37afdf7d4be2b2dc0b52e5a42ac2e8033a45145c096c03651cdfa19bb12f0b50853d467a8bf1807abde18c7b8de8e00bc6be8e6ae67cbd86cf082b5836c35792bafafba72926a48450c72885b9d81b9a6e5c92fd6e51e58b15c4fbfecbd3b062cc44e46afad308813900c79ecf5a573dd6c6b0d8e3bace110c1f51f3ddc15693a08fd56f8f4bffccc943beafa109c98df2348d2ba84c25b267645f22eab57624eafb14101d6e4449c9aff99e44614676fb724beeb7222399102bb1eaa7f7a813da09ac85602a8a8d2085f8b8178076c9f8d23d859a38abf231cc7816b57bc69f81bdb52844166134d82b5a0fbd7ffaa0225841376d0122b70f245cad60e9c84636eed03b47c659939f91bad9a012e89a47eac60f37747d60384deb97d78b2f469cc434fb70a8f1a44253366c5ccc432fdb532ad61d807f10459cafe2484ed67b213328cd8d95c794474fcdd2a4a2da1e9e50752da0c44d9eaf9eb5e45c5600a172a439c1001cd6343bd31a0de8387074b514c85814130e690e509819348734f212f001279b819bc1a11a8c6635405e6771158749fca768e6c6f56e1c0a69407570e1d67c802b37feefaf0fe703e9c3065c8fc18f3e401f53830e748356af10695ee15c8a37d020d05a047efb0d7cb6fb5b0afdf2fce752144bfce79dd8e574337517276860bd5ce2d0830f1493809d3b513c9f66c376a9eb0e3d4517bfc273c73f463d25cbe52e86018cabc9b42d39e0bfc6d0933a254f708ecba196c46b4160cc3f607fe678ceae0dc76b2441721f665005b281662dbf9eb699e961b3a91cedf03e1026d352c3031bdf3934a989f26c1d6fc862b149f2db3081c9200a2a9d7bf2168c92efe3327c32cd301cd7dcd376451ac6094913b0f90dd076084c0dec2fb0046a8b6bdccc0e9b8ec30927a8210517b1a865957dd9efbcabdeb647b205b712ff00a18b3b52ff592e237414066f0cd3517d5b0038f1ac03b9cd445489d5a4ba58814b7da7dd490d5cd86911d63b9542c03e703d05de5587e17b680d39489aebe2b49d7c4e7dfb100250ef4a8b09264bf35d9cadf34f5a793991d5797d9f24fd725fefadc6485e9845bb8edb811f0d691e4e9b478db02068e60dd2e2341d38479aded19dcb9d88e2751c2dea0aebd7b14f530966dbf8ea007df92e2f22517b133678c4b70239f0fb7d1103d2931cce1f24da86191c6afa45bbe26ba091e4311b6faf4df2d6a2c2b659007aa7e51e4ee2dcd21abc1415a86fc362a56ab0fcaa8efd7739923e69f261a42afa83db66864d5e67b87d1b242715065823f2fa0792ffbf02fddf7f058a3a3f1a7f038ff7cb1a0a656e6473747265616d0a656e646f626a0a312030206f626a0a3c3c2f436f6e74656e74732035203020522f547970652f506167652f5265736f75726365733c3c2f50726f63536574205b2f504446202f54657874202f496d61676542202f496d61676543202f496d616765495d2f466f6e743c3c2f46312032203020522f46322033203020522f46332034203020523e3e3e3e2f506172656e742036203020522f4d65646961426f785b30203020363132203739325d3e3e0a656e646f626a0a372030206f626a0a3c3c2f46696c7465722f466c6174654465636f64652f4c656e67746820313930333e3e73747265616d0a789ced5adb6edc36107dd75710794a1e9616a97b51f4214893a24080265da00f715128bb5aaf125d9cd53a41f235f9d40e2f92484ad6758df82136ecd578c9e1cc993387a2d69face75bcb464144d1766ffdbeb5de589f2c1bdb4ee0a12f16457fc29b1f2c62a3d7d6bb7f6db4b7dc1005ae8b72cbf35d7e95f12b37c436bb86b7954bf1fed1fac72ab863df4181e3630fa6c3a51f3ad80d980337d20d31a89e4910fb3edd2c987ee0cb36312bd1a941b7a9dc132a063402c7632f6c851053cdee048c197836804701bc770487c8c611e2f8d9d805c3879f10bd7dc57c3b219f4e1c062af8962b839d597faf889f45e7872ea674182f9e8d1cb82003193273184ab4202369aecba0b7da5a6299599c66d2e25a18895ca0142d41f21ef2e8f08fb5c2ecba98d45a59180deef174868bd5421410ee86fac2ad346d1b0702149bcf336d31bc23136b9d1d94066a323532e9d64d07a24d6d21f57d8fd40b232fa0b8e6837a2dc7f4e9e4cce9ab7452598b63e41105206974839ddd97301f5e5a220b7bbd467a01b4db20543c13396c7927320776d8088b34572a645f9db5b432ad2aed8cc57530d2b840191476e4266f74e8c71a60feae65906a65515aa84712192891298abe4b1b11f3c208475e2b5aa6cdc6de23874bdca842289232e2eed647c979b504b268a24070dbf5098eea0d5233e4a83e119ced60950c2aab7190c054b706c5ee063dbb095df6a796b6dc5c2f85ae4fb13f02184f460e5cde78cc81d38888b0564a616fb5b5bc32b334edacc595d013595f08851d790f7574f4479b61fe26a5136b5d6134b4c7b319ae95a98b9e43845b61ba418449d06a9869cbe1f7a8e37267aa4636991a9974cba603b15a295d073c09e6d37a076374d00c31a84f27674e5fa592ed5a1c221a693b876277029edd99e02ba40a9785bd5e25192ca1338c17cf460e5cde8db4bd356519d17a9f5da5937dd5d612cbcce2349316d7c248e402a5680992f79047877fac15e6ef5e06b5561646837b3c9de162993ae9525bb815268d6cec28e75ed396c3efd1c9e5ce549d6c323532e9d64d0762b54e52176ef7e56989383ea6f526aa1972549f52ce76b04a2b95d5384ef2e6a286a931bb21cfee4ff58e83af2cef23d66a2571026cd361c0783672e0f29e640efd5662a4b9522b7bebad659619c569272d2e8691c9056aa15024efd247c77fb41de6ef6206b9569646c57b349bc15a995249e1b6ce6fc48d780ef61431336d31fa1ea15cea4a95c93a4523876ec13404568b24b15decd58f94481d67a45dcb317d123973fa2a8154d6e210d9b0712808356637e0d93d09f389ca61615fe0e923659d368096382a50d18f8b8f7570466c0fa9cc58298c7d555613ca8c92b453963ff35073b800fe0a2df22e6534d8c7e83f7fb332e8b4aa242ad263990cd5c894c3c8e50ee5f3c31007ca09d830f9d07ba470891b5506ebc4f4d03b15d2b25e2e820ff401d78f7d92fc481edc3c9273d163b8e1f8f13dfe081ac346ec1b84e2f9d6ba7ac95411db046d0fa020ec0d82429baf410256c26d6e3dbd49cfe894e4e5394177b7fb185eca537a931668b3b93ddd1509baba126f57cfb61fd8bfb38c78061986146bcfef4f71b13ba2cd1efd2a2eff2be23cf90d9c66e52ece6a9f4bd0d353a52852837123d8407c689f90f058febaab8e2846873463f9a14399ed93133a97288fab7372fa45c671398d1846c977d8276e3548f17e8f5e42640c9a4920fb24c27ed4ccdf95790e2f9b1c3d015749ebac4df0c977f41d402f5899d303434220f025cdf8f2d396b57d26476cd98a7997ebb297a43883cb134a62a836437992432f72b0ef3779dcb22249f689b01f941e1ef4b32da8fa1618fe39e912e4702a73c4998ae2620f7d72dbcf988beafa0866947f38d1b46e8e36a779ecf1ec1047c4f0b039b4ac01a200478a0498040482b2ee92690471230ff7d2f2c4f15588c98195d49ce63a845e8e7e14555c38b0537f58496254dd26bbf490eed0f5d3a22c3622aeeb67520475c65c740b1e818e104c5ab13f24e75d839df17575b53da61553854c8e8be1aa3ca0f331a9778949e572a21013d15b22fba4e2faf0b5bc9b363ff4d8ff5835543a26bb8fe55dbd9f0896b63197a8fa92b268d3020ab1bbabcec02eb9f54c5b2ea098b42d9116f0eb9e2fbe1c1b90c659fa0dd4b4e0e02c59d48bb058f3fa69797b4ecb22ceae9f4d9beafad88e7a3790de78d900c6d8aa37dc694b3a0eb683bee6869a9e509e54557c933c69969403f862c7b8b881b5f3786fe2356d690a7ff715324d9b65c30da3a72bc6504df3f8631b2a44c9d37a959effb87b2f847fd2aa140e5c7253e9c3f861e489c24ee60a2abd4e4e376c239344e40d11c3b67f0491ead3a1cbdd838fe042d9c7de9d7ecebfca408d62acec676a4798aabb055331a1781c0a65d149fe48c49fde76c2df6c8024156fadbaf778f8a7a42a33b8a1c8a11a697103cd501cb27477aef86dc4c724b99db86ee8d4fb0ea7e3e7e454814e304556b69bc9f75b24e02f3d690028a996c865d3f0024c4553f085c6f378983e21c4c3bee8931749969cdb46d19be25287c26154608bf33a07a5174839274d3b7141ef0baae77181c0d7a661f7d515db73d3e250ae80f5e7e39b9f8f6f7e3ebee98980cd7963fd0f9095f76a0a656e6473747265616d0a656e646f626a0a382030206f626a0a3c3c2f436f6e74656e74732037203020522f547970652f506167652f5265736f75726365733c3c2f50726f63536574205b2f504446202f54657874202f496d61676542202f496d61676543202f496d616765495d2f466f6e743c3c2f46322033203020522f46332034203020523e3e3e3e2f506172656e742036203020522f4d65646961426f785b30203020363132203739325d3e3e0a656e646f626a0a392030206f626a0a3c3c2f46696c7465722f466c6174654465636f64652f4c656e677468203633343e3e73747265616d0a789cd5554d6fdb300cbdeb57103d6d87c892237fedb601cbb0013bb40bd043b18311cb893b7fb44ad2a07fa3879dfb5347caae633b5d93341bb0d908425a22f9de232ddfb20f532620885c9826ece3949db35b26b818071e6c980b5f70f19a49015fd9d57701095321044a41c13c5f592bb7960ab9201b973b66bdbe6097acb489fd3104d2e32186a3e9858a0714afa29e5d6f798a9340b7991f1d9cda922dde0eb22ee02d8ddfc0e4a80479c2b31542eef6fc1db89c8413289c8bc25d495c123c02ab9de00a1d1f7f215c7ca2dce3d0862b9704c5dc4d65f473f6ed04fc04da0b3191f7925a964bb3ed15f81bc09440345a219fc63d0dff739deeb1ca879d69635edd88018f3fd087ed7414cf4c4e5ffd3d2fc1d15d198ed5896de9a9bd9fcdcbbdda2a14489b46f875dadaf585e4519d5550dcd06fb6ef1c10a7264b3b2f4fcb74c064b76d7d21b6d4fe11858f8521806e5413bf0bcec48508a6294a4c0f25a808e730707d1efa302dd89b0b5dc68586182659aea13230a9f2449b776fa7d7f42df90b942caa3148fc44c92eb250d866473ec510b479b682e20e104f6a3141a937b5f50883cb71de2f97eb222be7705fad1f12c8b31f1a5615cc167139d7d0b0d9533a44b8ca968ea12ef4b0042b4f6aaaa2030413b7580e4b1de0a4ba5d56a3743f31c779fc9cc26aa18d4620716e749cdc63ab0e2be9053c90b6649b1eb292f43160f44d0571990cd4aaeeb4d9986ca50141ae971a7ea69599e9c3ea293af99e18c64902a3f5b12a8d25f7a336c7ac2a0afc1b157056b731212d9ace505bce0ecb2af1f4dc22bb592f178f3b03b473390e6db405ed0434838422d1a3c30a0b8ffbf55b667451a1aa247b13fa1f9f2f1473ce7e01207948a20a656e6473747265616d0a656e646f626a0a31302030206f626a0a3c3c2f436f6e74656e74732039203020522f547970652f506167652f5265736f75726365733c3c2f50726f63536574205b2f504446202f54657874202f496d61676542202f496d61676543202f496d616765495d2f466f6e743c3c2f46322033203020522f46332034203020523e3e3e3e2f506172656e742036203020522f4d65646961426f785b30203020363132203739325d3e3e0a656e646f626a0a31322030206f626a0a3c3c2f446573745b31203020522f58595a20302037343420305d2f5469746c65284769742f4269744275636b65742043686561747368656574292f506172656e74203131203020523e3e0a656e646f626a0a31312030206f626a0a3c3c2f547970652f4f75746c696e65732f436f756e7420312f4669727374203132203020522f4c617374203132203020523e3e0a656e646f626a0a322030206f626a0a3c3c2f537562747970652f54797065312f547970652f466f6e742f42617365466f6e742f48656c7665746963612f456e636f64696e672f57696e416e7369456e636f64696e673e3e0a656e646f626a0a332030206f626a0a3c3c2f537562747970652f54797065312f547970652f466f6e742f42617365466f6e742f48656c7665746963612d426f6c642f456e636f64696e672f57696e416e7369456e636f64696e673e3e0a656e646f626a0a342030206f626a0a3c3c2f537562747970652f54797065312f547970652f466f6e742f42617365466f6e742f436f75726965722f456e636f64696e672f57696e416e7369456e636f64696e673e3e0a656e646f626a0a362030206f626a0a3c3c2f4b6964735b3120302052203820302052203130203020525d2f547970652f50616765732f436f756e7420332f4954585428322e312e37293e3e0a656e646f626a0a31332030206f626a0a3c3c2f506167654d6f64652f5573654f75746c696e65732f547970652f436174616c6f672f4f75746c696e6573203131203020522f50616765732036203020523e3e0a656e646f626a0a31342030206f626a0a3c3c2f4d6f644461746528443a32303138303832313139303032342d303427303027292f4372656174696f6e4461746528443a32303138303832313139303032342d303427303027292f50726f647563657228695465787420322e312e37206279203154335854293e3e0a656e646f626a0a787265660a302031350a303030303030303030302036353533352066200a30303030303032323331203030303030206e200a30303030303035353730203030303030206e200a30303030303035363538203030303030206e200a30303030303035373531203030303030206e200a30303030303030303135203030303030206e200a30303030303035383337203030303030206e200a30303030303032343036203030303030206e200a30303030303034333737203030303030206e200a30303030303034353433203030303030206e200a30303030303035323434203030303030206e200a30303030303035353032203030303030206e200a30303030303035343131203030303030206e200a30303030303035393133203030303030206e200a30303030303035393936203030303030206e200a747261696c65720a3c3c2f496e666f203134203020522f4944205b3c33303934633237353939636331353231666136366530376266656531396532633e3c66653864663539336362326234356330326630653839356538323666343066363e5d2f526f6f74203133203020522f53697a652031353e3e0a7374617274787265660a363131390a2525454f460a', x'553d7a34', "2018-07-11 05:30:00"),
	(1, 2, x'453d7a38', x'453d7a39', "2018-07-13 03:22:00"),
	(3, 3, x'653d7a38', x'653e7a39', "2019-02-13 11:40:33");

CREATE TABLE authentications (username VARCHAR(30) NOT NULL,
                              passwordHash VARCHAR(40) NOT NULL,
                              isUser BOOLEAN NOT NULL);

INSERT INTO authentications
    (username, passwordHash, isUser)
VALUES
    ("johnnyappleseed", "hash", TRUE),
    ("timapple", "hash", TRUE),
    ("jeffbozo", "hash", TRUE),
    ("apple", "hash", FALSE),
    ("amazon", "hash", FALSE);
