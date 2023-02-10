package wasmcraft

import io.github.kawamuray.wasmtime._
import io.github.kawamuray.wasmtime.wasi.{WasiCtx, WasiCtxBuilder}
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = WasmCraft.MODID, modLanguage = "scala")
object WasmCraft {
  final val MODID = "wasmcraft"

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = {
    val engine = new Engine()
    val linker = new Linker(engine)
    WasiCtx.addToLinker(linker)
    val wasi = new WasiCtxBuilder().inheritStdio().build()
    val store = new Store(engine, wasi)

    val module = Module.fromFile(engine, "./wasm-src.wasm")
    linker.module(store, "", module)
    linker.get(store, "", "main").get().func().call(store, Val.fromI32(1), Val.fromI32(0))
  }
}
