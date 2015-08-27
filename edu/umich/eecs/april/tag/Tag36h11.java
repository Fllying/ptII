package edu.umich.eecs.april.tag;

/*
This file is a modified version of a file by the same name in the
april package, written by Associate Professor Edwin Olson (ebolson@umich.edu),
EECS, University of Michigan. This file is part of a modified subset of the
april package created by Professor Edward A. Lee (eal@eecs.berkeley.edu) that
removes all dependencies on external libraries and graphical classes
and renames the packages to follow Java naming conventions.
The original files were retrieved on July 28, 2015, from the GIT repository
at git://april.eecs.umich.edu/home/git/april.git.  The Java implementation
AprilTags is described here: https://april.eecs.umich.edu/wiki/index.php/AprilTags-Java.
The April project is described here: https://april.eecs.umich.edu/.

The original author (Edwin Olson) has stipulated the following license:

This file is licensed under the terms of the GPLv2 or successor.

Alternative licenses use may be available by contacting
ebolson@umich.edu.

 */

/** Tag family with 587 distinct codes.
    bits: 36,  minimum hamming: 11,  minimum complexity: 10

    Max bits corrected       False positive rate
            0                    0.000001 %
            1                    0.000032 %
            2                    0.000570 %
            3                    0.006669 %
            4                    0.056985 %
            5                    0.379011 %

    Generation time: 210507.523000 s

    Hamming distance between pairs of codes (accounting for rotation):

       0  0
       1  0
       2  0
       3  0
       4  0
       5  0
       6  0
       7  0
       8  0
       9  0
      10  0
      11  7191
      12  14401
      13  20567
      14  29161
      15  31975
      16  29179
      17  21104
      18  11447
      19  4923
      20  1590
      21  372
      22  73
      23  8
      24  0
      25  0
      26  0
      27  0
      28  0
      29  0
      30  0
      31  0
      32  0
      33  0
      34  0
      35  0
      36  0
 **/
public class Tag36h11 extends TagFamily {
    public Tag36h11() {
        super(36, 11, new long[] { 0xd5d628584L, 0xd97f18b49L, 0xdd280910eL,
                0xe479e9c98L, 0xebcbca822L, 0xf31dab3acL, 0x056a5d085L,
                0x10652e1d4L, 0x22b1dfeadL, 0x265ad0472L, 0x34fe91b86L,
                0x3ff962cd5L, 0x43a25329aL, 0x474b4385fL, 0x4e9d243e9L,
                0x5246149aeL, 0x5997f5538L, 0x683bb6c4cL, 0x6be4a7211L,
                0x7e3158eeaL, 0x81da494afL, 0x858339a74L, 0x8cd51a5feL,
                0x9f21cc2d7L, 0xa2cabc89cL, 0xadc58d9ebL, 0xb16e7dfb0L,
                0xb8c05eb3aL, 0xd25ef139dL, 0xd607e1962L, 0xe4aba3076L,
                0x2dde6a3daL, 0x43d40c678L, 0x5620be351L, 0x64c47fa65L,
                0x686d7002aL, 0x6c16605efL, 0x6fbf50bb4L, 0x8d06d39dcL,
                0x9f53856b5L, 0xadf746dc9L, 0xbc9b084ddL, 0xd290aa77bL,
                0xd9e28b305L, 0xe4dd5c454L, 0xfad2fe6f2L, 0x181a8151aL,
                0x26be42c2eL, 0x2e10237b8L, 0x405cd5491L, 0x7742eab1cL,
                0x85e6ac230L, 0x8d388cdbaL, 0x9f853ea93L, 0xc41ea2445L,
                0xcf1973594L, 0x14a34a333L, 0x31eacd15bL, 0x6c79d2dabL,
                0x73cbb3935L, 0x89c155bd3L, 0x8d6a46198L, 0x91133675dL,
                0xa708d89fbL, 0xae5ab9585L, 0xb9558a6d4L, 0xb98743ab2L,
                0xd6cec68daL, 0x1506bcaefL, 0x4becd217aL, 0x4f95c273fL,
                0x658b649ddL, 0xa76c4b1b7L, 0xecf621f56L, 0x1c8a56a57L,
                0x3628e92baL, 0x53706c0e2L, 0x5e6b3d231L, 0x7809cfa94L,
                0xe97eead6fL, 0x5af40604aL, 0x7492988adL, 0xed5994712L,
                0x5eceaf9edL, 0x7c1632815L, 0xc1a0095b4L, 0xe9e25d52bL,
                0x3a6705419L, 0xa8333012fL, 0x4ce5704d0L, 0x508e60a95L,
                0x877476120L, 0xa864e950dL, 0xea45cfce7L, 0x19da047e8L,
                0x24d4d5937L, 0x6e079cc9bL, 0x99f2e11d7L, 0x33aa50429L,
                0x499ff26c7L, 0x50f1d3251L, 0x66e7754efL, 0x96ad633ceL,
                0x9a5653993L, 0xaca30566cL, 0xc298a790aL, 0x8be44b65dL,
                0xdc68f354bL, 0x16f7f919bL, 0x4dde0e826L, 0xd548cbd9fL,
                0xe0439ceeeL, 0xfd8b1fd16L, 0x76521bb7bL, 0xd92375742L,
                0xcab16d40cL, 0x730c9dd72L, 0xad9ba39c2L, 0xb14493f87L,
                0x52b15651fL, 0x185409cadL, 0x77ae2c68dL, 0x94f5af4b5L,
                0x0a13bad55L, 0x61ea437cdL, 0xa022399e2L, 0x203b163d1L,
                0x7bba8f40eL, 0x95bc9442dL, 0x41c0b5358L, 0x8e9c6cc81L,
                0x0eb549670L, 0x9da3a0b51L, 0xd832a67a1L, 0xdcd4350bcL,
                0x4aa05fdd2L, 0x60c7bb44eL, 0x4b358b96cL, 0x067299b45L,
                0xb9c89b5faL, 0x6975acaeaL, 0x62b8f7afaL, 0x33567c3d7L,
                0xbac139950L, 0xa5927c62aL, 0x5c916e6a4L, 0x260ecb7d5L,
                0x29b7bbd9aL, 0x903205f26L, 0xae72270a4L, 0x3d2ec51a7L,
                0x82ea55324L, 0x11a6f3427L, 0x1ca1c4576L, 0xa40c81aefL,
                0xbddccd730L, 0x0e617561eL, 0x969317b0fL, 0x67f781364L,
                0x610912f96L, 0xb2549fdfcL, 0x06e5aaa6bL, 0xb6c475339L,
                0xc56836a4dL, 0x844e351ebL, 0x4647f83b4L, 0x0908a04f5L,
                0x7f51034c9L, 0xaee537fcaL, 0x5e92494baL, 0xd445808f4L,
                0x28d68b563L, 0x04d25374bL, 0x2bc065f65L, 0x96dc3ea0cL,
                0x4b2ade817L, 0x07c3fd502L, 0xe768b5cafL, 0x17605cf6cL,
                0x182741ee4L, 0x62846097cL, 0x72b5ebf80L, 0x263da6e13L,
                0xfa841bcb5L, 0x7e45e8c69L, 0x653c81fa0L, 0x7443b5e70L,
                0x0a5234afdL, 0x74756f24eL, 0x157ebf02aL, 0x82ef46939L,
                0x80d420264L, 0x2aeed3e98L, 0xb0a1dd4f8L, 0xb5436be13L,
                0x7b7b4b13bL, 0x1ce80d6d3L, 0x16c08427dL, 0xee54462ddL,
                0x1f7644cceL, 0x9c7b5cc92L, 0xe369138f8L, 0x5d5a66e91L,
                0x485d62f49L, 0xe6e819e94L, 0xb1f340eb5L, 0x09d198ce2L,
                0xd60717437L, 0x0196b856cL, 0xf0a6173a5L, 0x12c0e1ec6L,
                0x62b82d5cfL, 0xad154c067L, 0xce3778832L, 0x6b0a7b864L,
                0x4c7686694L, 0x5058ff3ecL, 0xd5e21ea23L, 0x9ff4a76eeL,
                0x9dd981019L, 0x1bad4d30aL, 0xc601896d1L, 0x973439b48L,
                0x1ce7431a8L, 0x57a8021d6L, 0xf9dba96e6L, 0x83a2e4e7cL,
                0x8ea585380L, 0xaf6c0e744L, 0x875b73babL, 0xda34ca901L,
                0x2ab9727efL, 0xd39f21b9aL, 0x8a10b742fL, 0x5f8952dbaL,
                0xf8da71ab0L, 0xc25f9df96L, 0x06f8a5d94L, 0xe42e63e1aL,
                0xb78409d1bL, 0x792229addL, 0x5acf8c455L, 0x2fc29a9b0L,
                0xea486237bL, 0xb0c9685a0L, 0x1ad748a47L, 0x03b4712d5L,
                0xf29216d30L, 0x8dad65e49L, 0x0a2cf09ddL, 0x0b5f174c6L,
                0xe54f57743L, 0xb9cf54d78L, 0x4a312a88aL, 0x27babc962L,
                0xb86897111L, 0xf2ff6c116L, 0x82274bd8aL, 0x97023505eL,
                0x52d46edd1L, 0x585c1f538L, 0xbddd00e43L, 0x5590b74dfL,
                0x729404a1fL, 0x65320855eL, 0xd3d4b6956L, 0x7ae374f14L,
                0x2d7a60e06L, 0x315cd9b5eL, 0xfd36b4eacL, 0xf1df7642bL,
                0x55db27726L, 0x8f15ebc19L, 0x992f8c531L, 0x62dea2a40L,
                0x928275cabL, 0x69c263cb9L, 0xa774cca9eL, 0x266b2110eL,
                0x1b14acbb8L, 0x624b8a71bL, 0x1c539406bL, 0x3086d529bL,
                0x0111dd66eL, 0x98cd630bfL, 0x8b9d1ffdcL, 0x72b2f61e7L,
                0x9ed9d672bL, 0x96cdd15f3L, 0x6366c2504L, 0x6ca9df73aL,
                0xa066d60f0L, 0xe7a4b8addL, 0x8264647efL, 0xaa195bf81L,
                0x9a3db8244L, 0x014d2df6aL, 0x0b63265b7L, 0x2f010de73L,
                0x97e774986L, 0x248affc29L, 0xfb57dcd11L, 0x0b1a7e4d9L,
                0x4bfa2d07dL, 0x54e5cdf96L, 0x4c15c1c86L, 0xcd9c61166L,
                0x499380b2aL, 0x540308d09L, 0x8b63fe66fL, 0xc81aeb35eL,
                0x86fe0bd5cL, 0xce2480c2aL, 0x1ab29ee60L, 0x8048daa15L,
                0xdbfeb2d39L, 0x567c9858cL, 0x2b6edc5bcL, 0x2078fca82L,
                0xadacc22aaL, 0xb92486f49L, 0x51fac5964L, 0x691ee6420L,
                0xf63b3e129L, 0x39be7e572L, 0xda2ce6c74L, 0x20cf17a5cL,
                0xee55f9b6eL, 0xfb8572726L, 0xb2c2de548L, 0xcaa9bce92L,
                0xae9182db3L, 0x74b6e5bd1L, 0x137b252afL, 0x51f686881L,
                0xd672f6c02L, 0x654146ce4L, 0xf944bc825L, 0xe8327f809L,
                0x76a73fd59L, 0xf79da4cb4L, 0x956f8099bL, 0x7b5f2655cL,
                0xd06b114a6L, 0xd0697ca50L, 0x27c390797L, 0xbc61ed9b2L,
                0xcc12dd19bL, 0xeb7818d2cL, 0x092fcecdaL, 0x89ded4ea1L,
                0x256a0ba34L, 0xb6948e627L, 0x1ef6b1054L, 0x8639294a2L,
                0xeda3780a4L, 0x39ee2af1dL, 0xcd257edc5L, 0x2d9d6bc22L,
                0x121d3b47dL, 0x37e23f8adL, 0x119f31cf6L, 0x2c97f4f09L,
                0xd502abfe0L, 0x10bc3ca77L, 0x53d7190efL, 0x90c3e62a6L,
                0x7e9ebf675L, 0x979ce23d1L, 0x27f0c98e9L, 0xeafb4ae59L,
                0x7ca7fe2bdL, 0x1490ca8f6L, 0x9123387baL, 0xb3bc73888L,
                0x3ea87e325L, 0x4888964aaL, 0xa0188a6b9L, 0xcd383c666L,
                0x40029a3fdL, 0xe1c00ac5cL, 0x39e6f2b6eL, 0xde664f622L,
                0xe979a75e8L, 0x7c6b4c86cL, 0xfd492e071L, 0x8fbb35118L,
                0x40b4a09b7L, 0xaf80bd6daL, 0x70e0b2521L, 0x2f5c54d93L,
                0x3f4a118d5L, 0x09c1897b9L, 0x079776eacL, 0x084b00b17L,
                0x3a95ad90eL, 0x28c544095L, 0x39d457c05L, 0x7a3791a78L,
                0xbb770e22eL, 0x9a822bd6cL, 0x68a4b1fedL, 0xa5fd27b3bL,
                0x0c3995b79L, 0xd1519dff1L, 0x8e7eee359L, 0xcd3ca50b1L,
                0xb73b8b793L, 0x57aca1c43L, 0xec2655277L, 0x785a2c1b3L,
                0x75a07985aL, 0xa4b01eb69L, 0xa18a11347L, 0xdb1f28ca3L,
                0x877ec3e25L, 0x31f6341b8L, 0x1363a3a4cL, 0x075d8b9baL,
                0x7ae0792a9L, 0xa83a21651L, 0x7f08f9fb5L, 0x0d0cf73a9L,
                0xb04dcc98eL, 0xf65c7b0f8L, 0x65ddaf69aL, 0x2cf9b86b3L,
                0x14cb51e25L, 0xf48027b5bL, 0x0ec26ea8bL, 0x44bafd45cL,
                0xb12c7c0c4L, 0x959fd9d82L, 0xc77c9725aL, 0x48a22d462L,
                0x8398e8072L, 0xec89b05ceL, 0xbb682d4c9L, 0xe5a86d2ffL,
                0x358f01134L, 0x8556ddcf6L, 0x67584b6e2L, 0x11609439fL,
                0x08488816eL, 0xaaf1a2c46L, 0xf879898cfL, 0x8bbe5e2f7L,
                0x101eee363L, 0x690f69377L, 0xf5bd93cd9L, 0xcea4c2bf6L,
                0x9550be706L, 0x2c5b38a60L, 0xe72033547L, 0x4458b0629L,
                0xee8d9ed41L, 0xd2f918d72L, 0x78dc39fd3L, 0x8212636f6L,
                0x7450a72a7L, 0xc4f0cf4c6L, 0x367bcddcdL, 0xc1caf8cc6L,
                0xa7f5b853dL, 0x9d536818bL, 0x535e021b0L, 0xa7eb8729eL,
                0x422a67b49L, 0x929e928a6L, 0x48e8aefccL, 0xa9897393cL,
                0x5eb81d37eL, 0x1e80287b7L, 0x34770d903L, 0x2eef86728L,
                0x59266ccb6L, 0x0110bba61L, 0x1dfd284efL, 0x447439d1bL,
                0xfece0e599L, 0x9309f3703L, 0x80764d1ddL, 0x353f1e6a0L,
                0x2c1c12dccL, 0xc1d21b9d7L, 0x457ee453eL, 0xd66faf540L,
                0x44831e652L, 0xcfd49a848L, 0x9312d4133L, 0x3f097d3eeL,
                0x8c9ebef7aL, 0xa99e29e88L, 0x0e9fab22cL, 0x4e748f4fbL,
                0xecdee4288L, 0xabce5f1d0L, 0xc42f6876cL, 0x7ed402ea0L,
                0xe5c4242c3L, 0xd5b2c31aeL, 0x286863be6L, 0x160444d94L,
                0x5f0f5808eL, 0xae3d44b2aL, 0x9f5c5d109L, 0x8ad9316d7L,
                0x3422ba064L, 0x2fed11d56L, 0xbea6e3e04L, 0x04b029eecL,
                0x6deed7435L, 0x3718ce17cL, 0x55857f5e2L, 0x2edac7b62L,
                0x085d6c512L, 0xd6ca88e0fL, 0x2b7e1fc69L, 0xa699d5c1bL,
                0xf05ad74deL, 0x4cf5fb56dL, 0x5725e07e1L, 0x72f18a2deL,
                0x1cec52609L, 0x48534243cL, 0x2523a4d69L, 0x35c1b80d1L,
                0xa4d7338a7L, 0x0db1af012L, 0xe61a9475dL, 0x05df03f91L,
                0x97ae260bbL, 0x32d627fefL, 0xb640f73c2L, 0x45a1ac9c6L,
                0x6a2202de1L, 0x57d3e25f2L, 0x5aa9f986eL, 0x0cc859d8aL,
                0xe3ec6cca8L, 0x54e95e1aeL, 0x446887b06L, 0x7516732beL,
                0x3817ac8f5L, 0x3e26d938cL, 0xaa81bc235L, 0xdf387ca1bL,
                0x0f3a3b3f2L, 0xb4bf69677L, 0xae21868edL, 0x81e1d2d9dL,
                0xa0a9ea14cL, 0x8eee297a9L, 0x4740c0559L, 0xe8b141837L,
                0xac69e0a3dL, 0x9ed83a1e1L, 0x5edb55ecbL, 0x07340fe81L,
                0x50dfbc6bfL, 0x4f583508aL, 0xcb1fb78bcL, 0x4025ced2fL,
                0x39791ebecL, 0x53ee388f1L, 0x7d6c0bd23L, 0x93a995fbeL,
                0x8a41728deL, 0x2fe70e053L, 0xab3db443aL, 0x1364edb05L,
                0x47b6eeed6L, 0x12e71af01L, 0x52ff83587L, 0x3a1575dd8L,
                0x3feaa3564L, 0xeacf78ba7L, 0x0872b94f8L, 0xda8ddf9a2L,
                0x9aa920d2bL, 0x1f350ed36L, 0x18a5e861fL, 0x2c35b89c3L,
                0x3347ac48aL, 0x7f23e022eL, 0x2459068fbL, 0xe83be4b73L });
    }
}
