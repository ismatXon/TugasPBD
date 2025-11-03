import java.util.Scanner;

class Menu {
    String nama;
    int harga;
    String kategori;

    Menu(String nama, int harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }
}

public class RestoranApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Data menu
        Menu[] menuList = {
            new Menu("Pecel Ayam", 25000, "Makanan"),
            new Menu("Ayam Goreng", 20000, "Makanan"),
            new Menu("Sate Ayam", 30000, "Makanan"),
            new Menu("Es Teh", 5000, "Minuman"),
            new Menu("Jus Jeruk", 10000, "Minuman"),
            new Menu("Kopi Hitam", 8000, "Minuman")
        };

        System.out.println("=== MENU RESTORAN ===");
        // System.out.println("Makanan ===");
        System.out.println( menuList[0].kategori);
        System.out.println("1. " + menuList[0].nama + " - Rp" + menuList[0].harga);
        System.out.println("2. " + menuList[1].nama + " - Rp" + menuList[1].harga);
        System.out.println("3. " + menuList[2].nama + " - Rp" + menuList[2].harga);
        System.out.println( menuList[3].kategori);
        System.out.println("4. " + menuList[3].nama + " - Rp" + menuList[3].harga);
        System.out.println("5. " + menuList[4].nama + " - Rp" + menuList[4].harga);
        System.out.println("6. " + menuList[5].nama + " - Rp" + menuList[5].harga);

        System.out.println("\nMasukkan pesanan (format: NamaMenu=Jumlah). Tekan ENTER kosong jika selesai.");

        // Input maksimal 4 pesanan
        String input1 = sc.nextLine();
        String input2 = input1.isEmpty() ? "" : sc.nextLine();
        String input3 = input2.isEmpty() ? "" : sc.nextLine();
        String input4 = input3.isEmpty() ? "" : sc.nextLine();

        int subtotal = 0;
        boolean adaMinuman = false;

        subtotal += hitungPesanan(input1, menuList);
        if (cekMinuman(input1, menuList)) adaMinuman = true;

        subtotal += hitungPesanan(input2, menuList);
        if (cekMinuman(input2, menuList)) adaMinuman = true;

        subtotal += hitungPesanan(input3, menuList);
        if (cekMinuman(input3, menuList)) adaMinuman = true;

        subtotal += hitungPesanan(input4, menuList);
        if (cekMinuman(input4, menuList)) adaMinuman = true;

        // Promo & diskon
        double diskon = 0;
        boolean promoMinuman = false;

        if (subtotal > 100000) {
            diskon = 0.1 * subtotal;
        }
        if (subtotal > 50000 && adaMinuman) {
            promoMinuman = true;
        }

        // Pajak & biaya layanan
        double pajak = 0.1 * subtotal;
        int biayaLayanan = 20000;

        // Total akhir
        double total = subtotal + pajak + biayaLayanan - diskon;

        // Cetak struk
        System.out.println("\n=== STRUK PEMBAYARAN ===");
        cetakPesanan(input1, menuList);
        cetakPesanan(input2, menuList);
        cetakPesanan(input3, menuList);
        cetakPesanan(input4, menuList);

        System.out.println("----------------------------");
        System.out.println("Subtotal: Rp" + subtotal);
        System.out.println("Pajak (10%): Rp" + pajak);
        System.out.println("Biaya Layanan: Rp" + biayaLayanan);

        if (diskon > 0) {
            System.out.println("Diskon 10%: -Rp" + diskon);
        }
        if (promoMinuman) {
            System.out.println("Promo Beli 1 Gratis 1 Minuman diterapkan!");
        }

        System.out.println("TOTAL BAYAR: Rp" + total);
        System.out.println("=== TERIMA KASIH ===");
    }

    // Fungsi bantu
    static int hitungPesanan(String input, Menu[] menuList) {
        if (input == null || input.isEmpty()) return 0;
        String[] parts = input.split("=");
        if (parts.length < 2) return 0;

        String nama = parts[0].trim();
        int jumlah = Integer.parseInt(parts[1].trim());

        for (Menu m : menuList) {
            if (m.nama.equalsIgnoreCase(nama)) {
                return m.harga * jumlah;
            }
        }
        return 0;
    }

    static boolean cekMinuman(String input, Menu[] menuList) {
        if (input == null || input.isEmpty()) return false;
        String[] parts = input.split("=");
        if (parts.length < 2) return false;

        String nama = parts[0].trim();
        for (Menu m : menuList) {
            if (m.nama.equalsIgnoreCase(nama) && m.kategori.equalsIgnoreCase("Minuman")) {
                return true;
            }
        }
        return false;
    }

    static void cetakPesanan(String input, Menu[] menuList) {
        if (input == null || input.isEmpty()) return;
        String[] parts = input.split("=");
        if (parts.length < 2) return;

        String nama = parts[0].trim();
        int jumlah = Integer.parseInt(parts[1].trim());

        for (Menu m : menuList) {
            if (m.nama.equalsIgnoreCase(nama)) {
                int totalItem = m.harga * jumlah;
                System.out.println(m.nama + " x" + jumlah + " @Rp" + m.harga + " = Rp" + totalItem);
            }
        }
    }
}
