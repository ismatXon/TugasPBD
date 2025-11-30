
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Manajemen Restoran - OOP, Polymorphism, Exception, File I/O, Menu Interaktif
 * Simpan file di satu berkas (Main.java) untuk kemudahan. Jalankan: javac Main.java && java Main
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Menu menu = new Menu();
    private static final Order currentOrder = new Order();

    public static void main(String[] args) {
        System.out.println("=== Sistem Manajemen Restoran ===");

        boolean running = true;
        while (running) {
            tampilMenuUtama();
            int pilihan = bacaInt("Pilih menu: ");

            switch (pilihan) {
                case 1 -> tambahItemKeMenu();
                case 2 -> tampilkanMenuRestoran();
                case 3 -> terimaPesanan();
                case 4 -> hitungTotalDenganDiskon();
                case 5 -> tampilkanDanSimpanStruk();
                case 6 -> simpanMenuKeFile();
                case 7 -> muatMenuDariFile();
                case 8 -> muatStrukDariFile();
                case 9 -> {
                    System.out.println("Keluar. Terima kasih!");
                    running = false;
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
            System.out.println();
        }
    }

    // === UI Helpers ===
    private static void tampilMenuUtama() {
        System.out.println("\n--- Menu Utama ---");
        System.out.println("1. Tambah item menu (Makanan/Minuman/Diskon)");
        System.out.println("2. Tampilkan menu restoran");
        System.out.println("3. Terima pesanan pelanggan");
        System.out.println("4. Hitung total biaya (dengan diskon)");
        System.out.println("5. Tampilkan & Simpan struk pesanan");
        System.out.println("6. Simpan menu ke file");
        System.out.println("7. Muat menu dari file");
        System.out.println("8. Muat struk dari file");
        System.out.println("9. Keluar");
    }

    private static int bacaInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = scanner.nextLine().trim();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Masukan harus angka. Coba lagi.");
            }
        }
    }

    private static double bacaDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = scanner.nextLine().trim();
                double d = Double.parseDouble(s);
                if (d < 0) throw new IllegalArgumentException("Nilai tidak boleh negatif.");
                return d;
            } catch (NumberFormatException e) {
                System.out.println("Masukan harus angka desimal. Coba lagi.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String bacaNonKosong(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("Masukan tidak boleh kosong.");
        }
    }

    // === Actions ===
    private static void tambahItemKeMenu() {
        System.out.println("\n--- Tambah Item ---");
        System.out.println("1. Makanan");
        System.out.println("2. Minuman");
        System.out.println("3. Diskon");
        int p = bacaInt("Pilih tipe item: ");

        switch (p) {
            case 1 -> {
                String nama = bacaNonKosong("Nama makanan: ");
                double harga = bacaDouble("Harga: ");
                String kategori = "Makanan";
                String jenis = bacaNonKosong("Jenis makanan (mis. Rumahan, Cepat saji): ");
                menu.tambahItem(new Food(nama, harga, kategori, jenis));
                System.out.println("Makanan ditambahkan.");
            }
            case 2 -> {
                String nama = bacaNonKosong("Nama minuman: ");
                double harga = bacaDouble("Harga: ");
                String kategori = "Minuman";
                String jenis = bacaNonKosong("Jenis minuman (mis. Dingin, Panas): ");
                menu.tambahItem(new Drink(nama, harga, kategori, jenis));
                System.out.println("Minuman ditambahkan.");
            }
            case 3 -> {
                String nama = bacaNonKosong("Nama diskon: ");
                double persen = bacaDouble("Persentase diskon (0-100): ");
                if (persen < 0 || persen > 100) {
                    System.out.println("Diskon harus 0-100.");
                    return;
                }
                String kategori = "Diskon";
                System.out.println("Tipe penerapan: 1=GLOBAL (semua), 2=Per kategori");
                int t = bacaInt("Pilih tipe: ");
                String target = (t == 2) ? bacaNonKosong("Kategori target (Makanan/Minuman): ") : "GLOBAL";
                menu.tambahItem(new Discount(nama, persen, kategori, target));
                System.out.println("Diskon ditambahkan.");
            }
            default -> System.out.println("Pilihan tipe tidak valid.");
        }
    }

    private static void tampilkanMenuRestoran() {
        System.out.println("\n--- Daftar Menu ---");
        if (menu.getItems().isEmpty()) {
            System.out.println("Menu kosong.");
            return;
        }
        int idx = 1;
        for (MenuItem item : menu.getItems()) {
            System.out.printf("%d) ", idx++);
            item.tampilMenu(); // polymorphism in action
        }
    }

    private static void terimaPesanan() {
        if (menu.getItems().isEmpty()) {
            System.out.println("Menu kosong. Tambah item terlebih dahulu.");
            return;
        }
        tampilkanMenuRestoran();
        System.out.println("\nMasukkan nomor item yang dipesan, 0 untuk selesai.");
        while (true) {
            int no = bacaInt("Nomor item: ");
            if (no == 0) break;
            try {
                MenuItem item = menu.getItemByIndex(no - 1);
                currentOrder.tambahItem(item);
                System.out.println(item.getNama() + " ditambahkan ke pesanan.");
            } catch (ItemNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void hitungTotalDenganDiskon() {
        if (currentOrder.getItems().isEmpty()) {
            System.out.println("Belum ada pesanan.");
            return;
        }
        double subtotal = currentOrder.hitungSubtotal();
        double diskon = currentOrder.hitungTotalDiskon(menu.getItems());
        double total = Math.max(0, subtotal - diskon);

        System.out.printf("Subtotal: Rp %.2f\n", subtotal);
        System.out.printf("Potongan diskon: Rp %.2f\n", diskon);
        System.out.printf("Total bayar: Rp %.2f\n", total);
    }

    private static void tampilkanDanSimpanStruk() {
        if (currentOrder.getItems().isEmpty()) {
            System.out.println("Belum ada pesanan.");
            return;
        }
        String struk = currentOrder.formatStruk(menu.getItems());
        System.out.println("\n--- Struk Pesanan ---");
        System.out.println(struk);

        String path = "receipt.txt";
        try {
            Files.writeString(Paths.get(path), struk);
            System.out.println("Struk disimpan ke " + path);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan struk: " + e.getMessage());
        }
    }

    private static void simpanMenuKeFile() {
        String path = "menu.txt";
        try {
            MenuFileIO.saveMenu(menu.getItems(), path);
            System.out.println("Menu disimpan ke " + path);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan menu: " + e.getMessage());
        }
    }

    private static void muatMenuDariFile() {
        String path = "menu.txt";
        try {
            List<MenuItem> loaded = MenuFileIO.loadMenu(path);
            menu.setItems(loaded);
            System.out.println("Menu dimuat dari " + path + ". Total item: " + loaded.size());
        } catch (IOException e) {
            System.out.println("Gagal memuat menu: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Format menu tidak valid: " + e.getMessage());
        }
    }

    private static void muatStrukDariFile() {
        String path = "receipt.txt";
        try {
            String content = Files.readString(Paths.get(path));
            System.out.println("\n--- Struk dari File ---");
            System.out.println(content);
        } catch (IOException e) {
            System.out.println("Gagal membaca struk: " + e.getMessage());
        }
    }
}

/** Abstract base class for all menu items */
abstract class MenuItem {
    private String nama;
    private double harga;
    private String kategori;

    public MenuItem(String nama, double harga, String kategori) {
        setNama(nama);
        setHarga(harga);
        setKategori(kategori);
    }

    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public String getKategori() { return kategori; }

    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) throw new IllegalArgumentException("Nama tidak boleh kosong.");
        this.nama = nama.trim();
    }

    public void setHarga(double harga) {
        if (harga < 0) throw new IllegalArgumentException("Harga tidak boleh negatif.");
        this.harga = harga;
    }

    public void setKategori(String kategori) {
        if (kategori == null || kategori.isBlank()) throw new IllegalArgumentException("Kategori tidak boleh kosong.");
        this.kategori = kategori.trim();
    }

    public abstract void tampilMenu();

    /** Untuk keperluan I/O serialisasi sederhana */
    public abstract String serialize();
}

/** Subclass: Food */
class Food extends MenuItem {
    private String jenisMakanan;

    public Food(String nama, double harga, String kategori, String jenisMakanan) {
        super(nama, harga, kategori);
        setJenisMakanan(jenisMakanan);
    }

    public String getJenisMakanan() { return jenisMakanan; }

    public void setJenisMakanan(String jenisMakanan) {
        if (jenisMakanan == null || jenisMakanan.isBlank())
            throw new IllegalArgumentException("Jenis makanan tidak boleh kosong.");
        this.jenisMakanan = jenisMakanan.trim();
    }

    @Override
    public void tampilMenu() {
        System.out.printf("[Makanan] %s - Rp %.2f | Jenis: %s\n", getNama(), getHarga(), jenisMakanan);
    }

    @Override
    public String serialize() {
        return String.format("FOOD|%s|%f|%s|%s", getNama(), getHarga(), getKategori(), jenisMakanan);
    }
}

/** Subclass: Drink */
class Drink extends MenuItem {
    private String jenisMinuman;

    public Drink(String nama, double harga, String kategori, String jenisMinuman) {
        super(nama, harga, kategori);
        setJenisMinuman(jenisMinuman);
    }

    public String getJenisMinuman() { return jenisMinuman; }

    public void setJenisMinuman(String jenisMinuman) {
        if (jenisMinuman == null || jenisMinuman.isBlank())
            throw new IllegalArgumentException("Jenis minuman tidak boleh kosong.");
        this.jenisMinuman = jenisMinuman.trim();
    }

    @Override
    public void tampilMenu() {
        System.out.printf("[Minuman] %s - Rp %.2f | Jenis: %s\n", getNama(), getHarga(), jenisMinuman);
    }

    @Override
    public String serialize() {
        return String.format("DRINK|%s|%f|%s|%s", getNama(), getHarga(), getKategori(), jenisMinuman);
    }
}

/** Subclass: Discount (persentase 0-100), target GLOBAL atau per kategori */
class Discount extends MenuItem {
    private double persen;
    private String target; // "GLOBAL" atau nama kategori (mis. "Minuman")

    public Discount(String nama, double persen, String kategori, String target) {
        super(nama, persen, kategori); // harga diisi dengan nilai persen untuk kesederhanaan serialisasi
        setPersen(persen);
        setTarget(target);
    }

    public double getPersen() { return persen; }
    public String getTarget() { return target; }

    public void setPersen(double persen) {
        if (persen < 0 || persen > 100) throw new IllegalArgumentException("Diskon harus 0-100.");
        this.persen = persen;
    }

    public void setTarget(String target) {
        if (target == null || target.isBlank()) throw new IllegalArgumentException("Target diskon tidak boleh kosong.");
        this.target = target.trim(); // "GLOBAL" atau kategori spesifik
    }

    @Override
    public void tampilMenu() {
        String infoTarget = target.equalsIgnoreCase("GLOBAL") ? "Semua item" : ("Kategori: " + target);
        System.out.printf("[Diskon] %s - %.2f%% | %s\n", getNama(), persen, infoTarget);
    }

    @Override
    public String serialize() {
        return String.format("DISCOUNT|%s|%f|%s|%s", getNama(), persen, getKategori(), target);
    }
}

/** Menu: mengelola koleksi item */
class Menu {
    private List<MenuItem> items = new ArrayList<>();

    public List<MenuItem> getItems() { return items; }
    public void setItems(List<MenuItem> newItems) {
        if (newItems == null) throw new IllegalArgumentException("List tidak boleh null.");
        this.items = new ArrayList<>(newItems);
    }

    public void tambahItem(MenuItem item) {
        if (item == null) throw new IllegalArgumentException("Item tidak boleh null.");
        items.add(item);
    }

    public MenuItem getItemByIndex(int index) throws ItemNotFoundException {
        if (index < 0 || index >= items.size())
            throw new ItemNotFoundException("Item tidak ditemukan pada indeks: " + (index + 1));
        return items.get(index);
    }
}

/** Order: menyimpan item yang dipesan, menghitung subtotal & diskon */
class Order {
    private final List<MenuItem> items = new ArrayList<>();

    public List<MenuItem> getItems() { return items; }

    public void tambahItem(MenuItem item) {
        if (item == null) throw new IllegalArgumentException("Item tidak boleh null.");
        items.add(item);
    }

    public double hitungSubtotal() {
        double sum = 0.0;
        for (MenuItem item : items) {
            if (!(item instanceof Discount)) {
                sum += item.getHarga();
            }
        }
        return sum;
    }

    /** Hitung total potongan dari semua Discount di menu (GLOBAL atau per kategori) */
    public double hitungTotalDiskon(List<MenuItem> menuItems) {
        double subtotal = hitungSubtotal();
        if (subtotal <= 0) return 0;

        // Kumpulkan total harga per kategori untuk diskon kategori
        Map<String, Double> subtotalPerKategori = new HashMap<>();
        for (MenuItem item : items) {
            if (!(item instanceof Discount)) {
                subtotalPerKategori.merge(item.getKategori(), item.getHarga(), Double::sum);
            }
        }

        double totalPotongan = 0.0;
        for (MenuItem mi : menuItems) {
            if (mi instanceof Discount d) {
                double persen = d.getPersen() / 100.0;
                if (d.getTarget().equalsIgnoreCase("GLOBAL")) {
                    totalPotongan += subtotal * persen;
                } else {
                    double subCat = subtotalPerKategori.getOrDefault(d.getTarget(), 0.0);
                    totalPotongan += subCat * persen;
                }
            }
        }
        // Batasi agar tidak negatif
        return Math.min(totalPotongan, subtotal);
    }

    public String formatStruk(List<MenuItem> menuItems) {
        StringBuilder sb = new StringBuilder();
        sb.append("Struk Pesanan\n");
        sb.append("============================\n");
        for (MenuItem item : items) {
            if (item instanceof Discount) continue;
            sb.append(String.format("%-20s Rp %8.2f (%s)\n", item.getNama(), item.getHarga(), item.getKategori()));
        }
        sb.append("----------------------------\n");
        double subtotal = hitungSubtotal();
        double potongan = hitungTotalDiskon(menuItems);
        double total = Math.max(0, subtotal - potongan);
        sb.append(String.format("Subtotal          : Rp %8.2f\n", subtotal));
        sb.append(String.format("Diskon            : Rp %8.2f\n", potongan));
        sb.append(String.format("Total Bayar       : Rp %8.2f\n", total));

        // Tampilkan info diskon terpakai
        boolean adaDiskon = false;
        for (MenuItem mi : menuItems) {
            if (mi instanceof Discount d) {
                if (!adaDiskon) {
                    sb.append("\nDiskon aktif:\n");
                    adaDiskon = true;
                }
                sb.append(String.format("- %s (%.2f%%, %s)\n", d.getNama(), d.getPersen(),
                        d.getTarget().equalsIgnoreCase("GLOBAL") ? "GLOBAL" : ("Kategori " + d.getTarget())));
            }
        }
        sb.append("============================\n");
        sb.append("Terima kasih!\n");
        return sb.toString();
    }
}

/** Custom exception */
class ItemNotFoundException extends Exception {
    public ItemNotFoundException(String message) { super(message); }
}

/** File I/O untuk Menu */
class MenuFileIO {

    public static void saveMenu(List<MenuItem> items, String path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (MenuItem item : items) {
            lines.add(item.serialize());
        }
        Files.write(Paths.get(path), lines);
    }

    public static List<MenuItem> loadMenu(String path) throws IOException {
        List<MenuItem> items = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(path));

        for (String line : lines) {
            if (line.isBlank()) continue;
            String[] parts = line.split("\\|");
            if (parts.length < 5) throw new IllegalArgumentException("Baris tidak valid: " + line);

            String type = parts[0].trim();
            String nama = parts[1].trim();
            double val = Double.parseDouble(parts[2].trim());
            String kategori = parts[3].trim();
            String extra = parts[4].trim();

            switch (type) {
                case "FOOD" -> items.add(new Food(nama, val, kategori, extra));
                case "DRINK" -> items.add(new Drink(nama, val, kategori, extra));
                case "DISCOUNT" -> items.add(new Discount(nama, val, kategori, extra));
                default -> throw new IllegalArgumentException("Tipe tidak dikenali: " + type);
            }
        }
        return items;
    }
}
