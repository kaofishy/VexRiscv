package vexriscv.demo

import vexriscv.plugin._
import vexriscv.{plugin, VexRiscv, VexRiscvConfig}
import spinal.core._

/**
  * Created by spinalvm on 15.06.17.
  */
object FormalSimple extends App{
  def cpu() = new VexRiscv(
    config = VexRiscvConfig(
      plugins = List(
        new FomalPlugin,
        new HaltOnExceptionPlugin,
        new PcManagerSimplePlugin(
          resetVector = 0x00000000l,
          relaxedPcCalculation = false
        ),
        new IBusSimplePlugin(
          interfaceKeepData = false,
          catchAccessFault = false
        ),
        new DBusSimplePlugin(
          catchAddressMisaligned = true,
          catchAccessFault = false
        ),
        new DecoderSimplePlugin(
          catchIllegalInstruction = true,
          forceLegalInstructionComputation = true
        ),
        new RegFilePlugin(
          regFileReadyKind = plugin.SYNC,
          zeroBoot = false
        ),
        new IntAluPlugin,
        new SrcPlugin(
          separatedAddSub = false,
          executeInsertion = false
        ),
        new FullBarrielShifterPlugin,
        new HazardSimplePlugin(
          bypassExecute           = false,
          bypassMemory            = false,
          bypassWriteBack         = false,
          bypassWriteBackBuffer   = false,
          pessimisticUseSrc       = false,
          pessimisticWriteRegFile = false,
          pessimisticAddressMatch = false
        ),
        new BranchPlugin(
          earlyBranch = false,
          catchAddressMisaligned = true,
          prediction = NONE
        ),
        new YamlPlugin("cpu0.yaml")
      )
    )
  )
  SpinalConfig(
    defaultConfigForClockDomains = ClockDomainConfig(
      resetKind = spinal.core.SYNC,
      resetActiveLevel = spinal.core.HIGH
    )
  ).generateVerilog(cpu())
}
