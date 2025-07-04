package ar.utnba.ddsi.depoautomatizado.models.entities.robots;


import ar.utnba.ddsi.depoautomatizado.models.entities.mercaderias.Mercaderia;
import ar.utnba.ddsi.depoautomatizado.models.entities.recorridos.comandos.AccionRecorrido;
import ar.utnba.ddsi.depoautomatizado.models.entities.recorridos.obstaculos.EstrategiaObstaculo;
import java.util.Stack;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Robot {
  private Long id;
  private boolean disponible;
  private Integer ultimosPasosIntentados;
  private Stack<AccionRecorrido> accionesEjecutadas = new Stack<>();
  private EstrategiaObstaculo estrategiaObstaculo;
  private InterfazControladoraDeRobot interfazControladoraDeRobot;

  Robot(Long id, InterfazControladoraDeRobot interfazControladoraDeRobot) {
    this.id = id;
    this.disponible = true;
    this.interfazControladoraDeRobot = interfazControladoraDeRobot;
  }

  //NOTA: Asumimos que solo al avanzar puede detectar un obstaculo, girar lo hace sosbre su posicion
  public void avanzar(Integer pasos) {
    try {
      this.ultimosPasosIntentados = pasos;
      interfazControladoraDeRobot.avanzar(pasos);
    } catch (ExcepcionDeObstaculo e) {
      estrategiaObstaculo.manejarObstaculo(this);
    }
  }

  public void girar(Integer grados) {
    interfazControladoraDeRobot.girar(grados);
  }

  public void agarrarMercaderia(Mercaderia mercaderia) {
    interfazControladoraDeRobot.agarrarMercaderia(mercaderia);
  }

  public void soltarMercaderia() {
    interfazControladoraDeRobot.soltarMercaderia();
  }

  public void esperar(long tiempoEspera) {
    interfazControladoraDeRobot.esperar(tiempoEspera);
  }

  public void reintentarUltimoMovimiento() {
    interfazControladoraDeRobot.avanzar(this.ultimosPasosIntentados);
  }

  public abstract void esquivarObstaculo();

  public void accionEjecutada(AccionRecorrido accionRecorrido) {
    accionesEjecutadas.add(accionRecorrido);
  }

  public void volverAlInicio() {
    accionesEjecutadas.forEach(accionRecorrido -> {
      accionRecorrido.accionInversa().ejecutar(this);
    });
  }
}