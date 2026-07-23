package Main;

//Özellikle interface kullanmamızın sebebi,bir şeyi produce ederken bütün EXCEPTION'ları implement etmek için kullanır, 
//4 sınıf da aktif olarak kullanılır void produce methodunda, main'deki 4 exception'da kullanılır.
public interface Producer {
    //RawMaterialProducer ve Factory sınıfının interface'i, üretmesini sağlamak için metodu buradan çağırıyor
    void produce(String productName, int quantity) throws Exception;
    //Eğer kaynak yetersizse InsufficientResourcesException sınıfını fırlatıyor
}



