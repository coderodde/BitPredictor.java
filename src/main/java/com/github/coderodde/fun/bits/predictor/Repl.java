//package com.github.coderodde.fun.bits.predictor;
//
//import java.util.Random;
//import java.util.Scanner;
//
//public final class Repl {
//    
//    private static final int MAXIMUM_PREDICTOR_LENGTH = 10_000;
//    
//    public static void main(final String[] args) {
//        final Scanner scanner = new Scanner(System.in);
//        final Random random = new Random();
//        boolean[] bitString = { false };
//        
//        final boolean[] learningBits = getRandomLearningBitString(random);
//        
//        long start = System.currentTimeMillis();
//        
//        BitPredictor predictor = 
//                new MachineLearningBitPredictor(
//                        learningBits, 
//                        random);
//        
//        long end = System.currentTimeMillis();
//        
//        System.out.printf(
//                """
//                Built the initial predictor of length %d in %d milliseconds.
//                """, 
//                learningBits.length, 
//                end - start);
//        
//        while (true) {
//            try {
//                System.out.print(">>> ");
//                
//                final String commandLine = scanner.nextLine().trim();
//                final String[] commandTokens = commandLine.split("\\s+");
//                final String command = commandTokens[0];
//
//                switch (command) {
//                    case "exit":
//                    case "quit":
//                        return;
//                        
//                    case "new":
//                        final String subCommand = commandTokens[1];
//                        
//                        if (subCommand.equals("string")) {
//                            
//                            int length = Integer.parseInt(commandTokens[2]);
//                            
//                            bitString = getRandomBitString(length, random);
//                            
//                        } else if (subCommand.equals("predictor")) {
//                            
//                            int patternLength = 
//                                    Integer.parseInt(commandTokens[2]);
//                            
//                            int bitStringLength = 
//                                    Integer.parseInt(commandTokens[3]);
//                            
//                            predictor = 
//                                    new MachineLearningBitPredictor(
//                                        getRandomBitString(bitStringLength, 
//                                                           random),
//                                        patternLength, 
//                                        random);
//                        }
//                        
//                        break;
//                        
//                    case "dump":
//                        System.out.println(predictor);
//                        break;
//                        
//                    case "str":
//                        System.out.println(convertBitStringToString(bitString));
//                        break;
//                        
//                    case "predict":
//                        final boolean[] bitStringToPredict = 
//                                convertStringToBitString(commandTokens[1]);
//                        
//                        final boolean result = 
//                                predictor.predict(bitStringToPredict);
//                        
//                        System.out.println(result);
//                        break;
//                       
//                    default:
//                        System.out.printf(
//                                "Warning: unknown command \"%s\"",
//                                command);
//                }
//            } catch (final Exception ex) {
//                
//            }
//        }
//    }
//    
//    private static boolean[] getRandomBitString(final int length,
//                                                final Random random) {
//        final boolean[] bitString = new boolean[length];
//        
//        for (int i = 0; i < length; i++) {
//            bitString[i] = random.nextBoolean();
//        }
//        
//        return bitString;
//    }
//    
//    private static boolean[] getRandomLearningBitString(final Random random) {
//        return getRandomBitString(
//                random.nextInt(MAXIMUM_PREDICTOR_LENGTH),
//                random);
//    }
//    
//    private static String convertBitStringToString(final boolean[] bitString) {
//        final StringBuilder stringBuilder = new StringBuilder(bitString.length);
//        
//        for (final boolean bit : bitString) {
//            stringBuilder.append((bit ? "1" : "0"));
//        }
//        
//        return stringBuilder.toString();
//    }
//    
//    private static boolean[] convertStringToBitString(final String string) {
//        final boolean[] bitString = new boolean[string.length()];
//        
//        for (int i = 0; i < string.length(); i++) {
//            final char ch = string.charAt(i);
//            
//            switch (ch) {
//                case '0':
//                    bitString[i] = false;
//                    break;
//                    
//                case '1':
//                    bitString[i] = true;
//                    break;
//                    
//                default:
//                    throw new IllegalStateException(
//                            String.format(
//                                    "Not a bit character: %c.", 
//                                    ch));
//            }
//        }
//        
//        return bitString;
//    }
//    
//    private static int getRandomPredictorLength(final Random random) {
//        return random.nextInt(MAXIMUM_PREDICTOR_LENGTH);
//    }
//}
