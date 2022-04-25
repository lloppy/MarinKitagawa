import com.soywiz.klock.*
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.tween.*
import com.soywiz.korge.ui.UiSkinType.Companion.DOWN
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
	val walkReadyAnimation:SpriteAnimation = adventurerSprites.getSpriteAnimation(prefix = "walk-")
	val walkBackReadyAnimation:SpriteAnimation = adventurerSprites.getSpriteAnimation(prefix = "walkback-")
	val downReadyAnimation:SpriteAnimation = adventurerSprites.getSpriteAnimation(prefix = "down")
	val rowJumpAnimation:SpriteAnimation = adventurerSprites.getSpriteAnimation(prefix = "rowjump")
	val attackReadyAnimation:SpriteAnimation = adventurerSprites.getSpriteAnimation(prefix = "attack-ready")
	val walkintoAnimation:SpriteAnimation = adventurerSprites.getSpriteAnimation(prefix = "walkinto-")

	val adventurer: Sprite = sprite(idleAnimation).scale(4.0).xy(100.0, 100.0)
	adventurer.playAnimationLooped(spriteDisplayTime = 200.milliseconds)
	adventurer.onAnimationCompleted {
		playerStatus = PlayerStatus.IDLE
		adventurer.playAnimationLooped(idleAnimation)
	}

	addUpdater {
		if (views.input.keys.pressing(Key.X)) {
			playerStatusPlayAnimation(adventurer, attackReadyAnimation, PlayerStatus.ATTACK_READY)
		} else if (views.input.keys.pressing(Key.E)) {
			playerStatusPlayAnimation(adventurer, rowJumpAnimation, PlayerStatus.DOWN)
		} else if (views.input.keys.pressing(Key.RIGHT) or views.input.keys.pressing(Key.D)) {
			playerStatusPlayAnimation(adventurer, walkReadyAnimation, PlayerStatus.WALK)
		} else if (views.input.keys.pressing(Key.LEFT) or views.input.keys.pressing(Key.A)) {
			playerStatusPlayAnimation(adventurer, walkBackReadyAnimation, PlayerStatus.WALKBACK)
		} else if (views.input.keys.pressing(Key.DOWN) or views.input.keys.pressing(Key.S)) {
			playerStatusPlayAnimation(adventurer, downReadyAnimation, PlayerStatus.DOWN)
		} else if (views.input.keys.pressing(Key.UP) or views.input.keys.pressing(Key.W)) {
			playerStatusPlayAnimation(adventurer, walkintoAnimation, PlayerStatus.WALKINTO)
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
		PlayerStatus.WALKINTO -> {
			sprite.playAnimation(animation)
			sprite.y -= 2
		}
		PlayerStatus.ROWJUMP -> {
			sprite.playAnimation(animation)
			sprite.x += 2
		}
		PlayerStatus.WALKBACK -> {
		sprite.playAnimation(animation)
		sprite.x -= 2
		}
		PlayerStatus.DOWN -> {
			sprite.playAnimationLooped(animation)
		}
		PlayerStatus.ATTACK_READY -> {
			sprite.playAnimation(animation)
		}
	}
}