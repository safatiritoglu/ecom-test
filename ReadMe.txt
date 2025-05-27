Projenin Çalıştırılabilmesi İçin Gereken Minimum Gereksinimler ve Yönergeler:

1. Gereksinimler:
- Java 11 veya üzeri
- Maven 3.6 veya üzeri
- Chrome tarayıcı (sürüm 114 ve üzeri önerilir)
- Uygun sürümde ChromeDriver (manuel olarak C:\WebDriver\chromedriver.exe konumuna yerleştirilmelidir)

2. Yapılandırma:
- Tarayıcı ayarı src/test/resources/config.properties dosyasında yapılır:
  browser=chrome

3. Testleri Çalıştırma:
- Projeyi Eclipse veya IntelliJ gibi bir IDE'de açın.
- Testleri çalıştırmak için src/test/java/TestRun/TestRunner.java dosyasına sağ tıklayıp:
  Run As > JUnit Test seçeneğini kullanın.
- API testleri varsayılan olarak çalışır ancak gerçek endpoint bulunmadığı için bağlantı hatası alınabilir.

4. Test Çıktıları:
- API testleri başarılı olursa response verileri output/ klasörüne yazılır.
  - output/token.txt
  - output/viewInvoice.txt
  - output/sendInvoice.txt

Notlar: 
- Proje Cucumber + Selenium + RestAssured kullanılarak yapılandırılmıştır.
- *Bonus'lar projeye eklenmemiştir.
- Trendyol.com gerçek bir kullanıcı ile giriş yapılmayıp örnek kullanıcı bilgileri doldurulup sonraki adıma geçilmiştir.
- Filtreleme sonrası en alt satırdaki ürünlerde birden fazla satıcı bulunmadığı için en üst satırdan rastgele bir ürün seçilmiştir.