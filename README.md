# ChatEnhancer

Simple and extensible chat bot for Spigot/Paper Minecraft servers written in Kotlin.

[![Maintainability](https://api.codeclimate.com/v1/badges/a91189854f5a5703a910/maintainability)](https://codeclimate.com/github/ChemistsMC/ChatEnhancer/maintainability)
![Hex.pm](https://img.shields.io/github/release/ChemistsMC/ChatEnhancer.svg)
![Hex.pm](https://img.shields.io/badge/license-MIT-green.svg)

## Features
- [x] Mention players in chat messages
- [x] Search and replace text in previous chat messages
  - `.r [name] s/search/replace`
- [x] Verbally slap other players (all in good fun, of course!)
  - `.slap Name`
- [x] URL parsing for messages to display page title
  - [x] Show upvotes and downvotes for Reddit posts
  
#### Settings
- [x] Ability to enable/disable each module
- [x] Prefix used in messages from the ChatBot (its name)
- [x] Colors used in messages from the ChatBot
- [x] Color for mentions and highlighted messages

ChatEnhancer is designed to be extended by other plugins without too much trouble. Parts of this plugin are inspired by the [Narwhal IRC bot](https://github.com/narwhalirc/narwhal/) by JoshStrobl (especially the slap command!).

## Plans
#### Quoting
- [ ] Add chat quotes to a local database
  - `.quote Name` adds the last thing that player said
- [ ] Get and show a random quote from the database
  - `.quote`
  
## Building
ChatEnhancer is a Maven project, so to build simply clone the repo and run `mvn clean install`.

## License
ChatEnhancer is licensed under the MIT license. See the `LICENSE` file for details.
