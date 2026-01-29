package ecom.icet.Util;

public class IdGenerator {
        public static String generateNextId(String lastId, String prefix) {
            if (lastId == null || lastId.isEmpty()) {
                return prefix + "001";
            }

            String numericPart = lastId.substring(prefix.length());

            int nextId = Integer.parseInt(numericPart) + 1;

            return prefix + String.format("%03d", nextId);
        }
}

