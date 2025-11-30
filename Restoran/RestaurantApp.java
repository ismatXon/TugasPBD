import java.util.*;

class MenuItem {
    String nama;
    double harga;
    String kategori; // "Makanan" atau "Minuman"

    MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }
}

public class RestaurantApp {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<MenuItem> daftarMenu = new ArrayList<>();
    static ArrayList<MenuItem> pesanan = new ArrayList<>();
    static ArrayList<Integer> jumlahPesanan = new ArrayList<>();

    public static void main(String[] args) {
        // Data awal menu
        daftarMenu.add(new MenuItem("Nasi Goreng", 25000, "Makanan"));
        daftarMenu.add(new MenuItem("Ayam Bakar", 30000, "Makanan"));
        daftarMenu.add(new MenuItem("Ayam Goreng", 25000, "Makanan"));
        daftarMenu.add(new MenuItem("Es Teh", 5000, "Minuman"));
        daftarMenu.add(new MenuItem("Jus Jeruk", 10000, "Minuman"));
        daftarMenu.add(new MenuItem("Kopi", 7000, "Minuman"));

        while (true) {
            System.out.println("\n=== APLIKASI RESTORAN ===");
            System.out.println("1. Menu Pelanggan");
            System.out.println("2. Menu Pemilik");
            System.out.println("3. Keluar");
            System.out.print("Pilih: ");
            int pilih = sc.nextInt();
            sc.nextLine();

            switch (pilih) {
                case 1: menuPelanggan(); break;
                case 2: menuPemilik(); break;
                case 3: System.exit(0);
                default: System.out.println("Pilihan tidak valid!");
            }
        }
    }

    // ================= MENU PELANGGAN =================
    static void menuPelanggan() {
        pesanan.clear();
        jumlahPesanan.clear();

        while (true) {
            tampilkanMenu();
            System.out.print("Masukkan nama menu (atau 'selesai'): ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("selesai")) {
                cetakStruk();
                break;
            }

            MenuItem item = cariMenu(input);
            if (item != null) {
                System.out.print("Jumlah: ");
                int jumlah = sc.nextInt();
                sc.nextLine();
                pesanan.add(item);
                jumlahPesanan.add(jumlah);
            } else {
                System.out.println("Menu tidak ditemukan, coba lagi!");
            }
        }
    }

    static void tampilkanMenu() {
        System.out.println("\n=== Daftar Menu ===");
        for (int i = 0; i < daftarMenu.size(); i++) {
            MenuItem m = daftarMenu.get(i);
            System.out.println((i+1) + ". " + m.nama + " - Rp " + m.harga + " (" + m.kategori + ")");
        }
    }

    static MenuItem cariMenu(String nama) {
        for (MenuItem m : daftarMenu) {
            if (m.nama.equalsIgnoreCase(nama)) return m;
        }
        return null;
    }

    static void cetakStruk() {
        System.out.println("\n=== STRUK PEMESANAN ===");
        double total = 0;
        for (int i = 0; i < pesanan.size(); i++) {
            MenuItem m = pesanan.get(i);
            int jumlah = jumlahPesanan.get(i);
            double subtotal = m.harga * jumlah;
            System.out.println(m.nama + " x" + jumlah + " = Rp " + subtotal);
            total += subtotal;
        }

        // Diskon & promo
        boolean diskon10 = total > 100000;
        boolean promoMinuman = total > 50000;

        if (diskon10) {
            System.out.println("Diskon 10%: -Rp " + (total * 0.1));
            total *= 0.9;
        }

        if (promoMinuman) {
            System.out.println("Promo Beli 1 Gratis 1 (Minuman)");
            // logika sederhana: kurangi harga minuman termurah
            double minHarga = Double.MAX_VALUE;
            for (int i = 0; i < pesanan.size(); i++) {
                if (pesanan.get(i).kategori.equalsIgnoreCase("Minuman")) {
                    minHarga = Math.min(minHarga, pesanan.get(i).harga);
                }
            }
            if (minHarga != Double.MAX_VALUE) {
                System.out.println("Potongan promo: -Rp " + minHarga);
                total -= minHarga;
            }
        }

        double pajak = total * 0.1;
        double service = 20000;
        double grandTotal = total + pajak + service;

        System.out.println("Pajak (10%): Rp " + pajak);
        System.out.println("Biaya pelayanan: Rp " + service);
        System.out.println("TOTAL BAYAR: Rp " + grandTotal);
    }

    // ================= MENU PEMILIK =================
    static void menuPemilik() {
        while (true) {
            System.out.println("\n=== MENU PEMILIK ===");
            System.out.println("1. Tambah Menu");
            System.out.println("2. Ubah Harga");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali");
            System.out.print("Pilih: ");
            int pilih = sc.nextInt();
            sc.nextLine();

            switch (pilih) {
                case 1: tambahMenu(); break;
                case 2: ubahHarga(); break;
                case 3: hapusMenu(); break;
                case 4: return;
                default: System.out.println("Pilihan tidak valid!");
            }
        }
    }

    static void tambahMenu() {
        System.out.print("Nama menu: ");
        String nama = sc.nextLine();
        System.out.print("Harga: ");
        double harga = sc.nextDouble();
        sc.nextLine();
        System.out.print("Kategori (Makanan/Minuman): ");
        String kategori = sc.nextLine();
        daftarMenu.add(new MenuItem(nama, harga, kategori));
        System.out.println("Menu berhasil ditambahkan!");
    }

    static void ubahHarga() {
        tampilkanMenu();
        System.out.print("Pilih nomor menu untuk diubah: ");
        int idx = sc.nextInt() - 1;
        sc.nextLine();
        if (idx >= 0 && idx < daftarMenu.size()) {
            System.out.print("Yakin ubah harga? (Ya/Tidak): ");
            String konfirmasi = sc.nextLine();
            if (konfirmasi.equalsIgnoreCase("Ya")) {
                System.out.print("Harga baru: ");
                double hargaBaru = sc.nextDouble();
                sc.nextLine();
                daftarMenu.get(idx).harga = hargaBaru;
                System.out.println("Harga berhasil diubah!");
            }
        }
    }

    static void hapusMenu() {
        tampilkanMenu();
        System.out.print("Pilih nomor menu untuk dihapus: ");
        int idx = sc.nextInt() - 1;
        sc.nextLine();
        if (idx >= 0 && idx < daftarMenu.size()) {
            System.out.print("Yakin hapus menu? (Ya/Tidak): ");
            String konfirmasi = sc.nextLine();
            if (konfirmasi.equalsIgnoreCase("Ya")) {
                daftarMenu.remove(idx);
                System.out.println("Menu berhasil dihapus!");
            }
        }
    }
}
