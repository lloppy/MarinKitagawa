import com.soywiz.klock.*
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.Atlas
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import com.soywiz.korma.interpolation.*

var playerStatus: PlayerStatus = PlayerStatus.IDLE

suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {
	val adventurerSprites: Atlas = resourcesVfs["adventurer.xml"].readAtlas()

	val idleAnimation:SpriteAnimation = adventurerSprites.getSpriteAnimation(prefix = "idle")
	val walkReadyAnimation:SpriteAnimation = adventurerSprites.getSpriteAnimation(prefix = "walk")
	val attackReadyAnimation:SpriteAnimation = adventurerSprites.getSpriteAnimation(prefix = "attack-ready")

	val adventurer: Sprite = sprite(idleAnimation).scale(4.0).xy(100.0, 100.0)
	adventurer.playAnimationLooped(spriteDisplayTime = 200.milliseconds)
	adventurer.onAnimationCompleted {
		playerStatus = PlayerStatus.IDLE
		adventurer.playAnimationLooped(idleAnimation)
	}

	addUpdater {
		if (views.input.keys.pressing(Key.A)) {
			playerStatusPlayAnimation(adventurer, attackReadyAnimation, PlayerStatus.ATTACK_READY)
		} else if (views.input.keys.pressing(Key.SPACE)) {
			playerStatusPlayAnimation(adventurer, walkReadyAnimation, PlayerStatus.WALK)
		} else {
			playerStatusPlayAnimation(adventurer, idleAnimation, PlayerStatus.IDLE)
		}
	}
}

fun playerStatusPlayAnimation(sprite: Sprite, animation: SpriteAnimation, paramPlayerStatus: PlayerStatus) {
	playerStatus = paramPlayerStatus

	when (playerStatus) {
		PlayerStatus.IDLE -> {
			sprite.playAnimation(animation)
		}
		PlayerStatus.WALK -> {
			sprite.playAnimation(animation)
			sprite.x += 2
		}
		PlayerStatus.ATTACK_READY -> {
			sprite.playAnimation(animation)
		}
	}
}