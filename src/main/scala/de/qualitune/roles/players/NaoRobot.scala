package de.qualitune.roles.players

import com.aldebaran.proxy._
import de.qualitune.Nao
import de.qualitune.roles.{ComponentCore, ComponentRole, Component}

/**
 * Companion object for {@link de.qualitune.roles.players.NaoRobot}.
 * Provides standard IP and PORT and a static factory method
 * to create a new {@link de.qualitune.roles.ComponentCore} with NaoRobot as played {@link de.qualitune.roles.ComponentRole}.
 *
 * @author Max Leuthaeuser
 * @since 25.04.12
 */
object NaoRobot {
  val STANDARD_IP: String = "192.168.0.139"
  val STANDARD_PORT: Int = 8070

  def createWithCore(name: String, ip: String, port: Int) = {
    val core = new ComponentCore()
    val robot = new NaoRobot(name, ip, port, core)
    core.addRole(robot)
    core
  }
}

/**
 * Role class representing a NAO robot.
 *
 * @author Max Leuthaeuser
 * @since 25.04.12
 */
class NaoRobot(override val name: String = "Nao", val ip: String = NaoRobot.STANDARD_IP, val port: Int = NaoRobot.STANDARD_PORT, private val core: Component) extends ComponentRole(core) with Robot {
  def AudioDevice(): ALAudioDeviceProxy = Nao.createALAudioDevice(ip, port)

  def AudioPlayer(): ALAudioPlayerProxy = Nao.createALAudioPlayer(ip, port)

  def AudioSourceLocalization(): ALAudioSourceLocalizationProxy = Nao.createALAudioSourceLocalization(ip, port)

  def BehaviorManager(): ALBehaviorManagerProxy = Nao.createALBehaviorManager(ip, port)

  def Bluetooth(): ALBluetoothProxy = Nao.createALBluetooth(ip, port)

  def Bonjour(): ALBonjourProxy = Nao.createALBonjour(ip, port)

  def FaceDetection(): ALFaceDetectionProxy = Nao.createALFaceDetection(ip, port)

  def FaceTracker(): ALFaceTrackerProxy = Nao.createALFaceTracker(ip, port)

  def FileManager(): ALFileManagerProxy = Nao.createALFileManager(ip, port)

  def FrameManager(): ALFrameManagerProxy = Nao.createALFrameManager(ip, port)

  def Fsr(): ALFsrProxy = Nao.createALFsr(ip, port)

  def Infrared(): ALInfraredProxy = Nao.createALInfrared(ip, port)

  def LandMarkDetection(): ALLandMarkDetectionProxy = Nao.createALLandMarkDetection(ip, port)

  def Laser(): ALLaserProxy = Nao.createALLaser(ip, port)

  def Launcher(): ALLauncherProxy = Nao.createALLauncher(ip, port)

  def Leds(): ALLedsProxy = Nao.createALLeds(ip, port)

  def Logger(): ALLoggerProxy = Nao.createALLogger(ip, port)

  def Memory(): ALMemoryProxy = Nao.createALMemory(ip, port)

  def Motion(): ALMotionProxy = Nao.createALMotion(ip, port)

  def MotionRecorder(): ALMotionRecorderProxy = Nao.createALMotionRecorder(ip, port)

  def Preferences(): ALPreferencesProxy = Nao.createALPreferences(ip, port)

  def PythonBridge(): ALPythonBridgeProxy = Nao.createALPythonBridge(ip, port)

  def RedBallDetection(): ALRedBallDetectionProxy = Nao.createALRedBallDetection(ip, port)

  def RedBallTracker(): ALRedBallTrackerProxy = Nao.createALRedBallTracker(ip, port)

  def ResourceManager(): ALResourceManagerProxy = Nao.createALResourceManager(ip, port)

  def RobotPose(): ALRobotPoseProxy = Nao.createALRobotPose(ip, port)

  def Sensors(): ALSensorsProxy = Nao.createALSensors(ip, port)

  def Sentinel(): ALSentinelProxy = Nao.createALSentinel(ip, port)

  def Sonar(): ALSonarProxy = Nao.createALSonar(ip, port)

  def SoundDetection(): ALSoundDetectionProxy = Nao.createALSoundDetection(ip, port)

  def SpeechRecognition(): ALSpeechRecognitionProxy = Nao.createALSpeechRecognition(ip, port)

  def TextToSpeech(): ALTextToSpeechProxy = Nao.createALTextToSpeech(ip, port)

  def VideoDevice(): ALVideoDeviceProxy = Nao.createALVideoDevice(ip, port)

  def VisionRecognition(): ALVisionRecognitionProxy = Nao.createALVisionRecognition(ip, port)

  def VisionToolbox(): ALVisionToolboxProxy = Nao.createALVisionToolbox(ip, port)

  def DCM(): DCMProxy = Nao.createDCM(ip, port)
}
