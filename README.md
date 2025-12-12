# ChallengeAPI

Create fun custom challenges with modifiers. It's also possible to create your own modifiers as
plugins and register them with `ChallengeAPI`.

# How challenges work

When a player issues the `/challenge create` command, an inventory appears, where the player can
select the modifiers they want to add to the challenge.

When a challenge starts, the associated modifiers are instantiated and called.