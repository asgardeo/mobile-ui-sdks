# Contributing to Asgardeo Mobile SDKs

We would love for you to contribute to our mobile SDKs and help make it even better than it is today!
As a contributor, here are the guidelines we would like you to follow:

- [Commit Message Guidelines](#commit-message-guidelines)
- [Releases](#releases)

## Commit Message Guidelines

*This specification is inspired by and supersedes the [AngularJS commit message format][commit-message-format].*

We have very precise rules over how our Git commit messages must be formatted.
This format leads to **easier to read commit history**.

Each commit message consists of a **header**, a **body**, and a **footer**.

```
<header>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

The `header` is mandatory and must conform to the [Commit Message Header](#commit-header) format.

The `body` is mandatory for all commits except for those of type "docs".
When the body is present it must be at least 20 characters long and must conform to the [Commit Message Body](#commit-body) format.

The `footer` is optional. The [Commit Message Footer](#commit-footer) format describes what the footer is used for and the structure it must have.


#### <a name="commit-header"></a>Commit Message Header

```
<type>(<scope>): <short summary>
  │       │             │
  │       │             └─⫸ Summary in present tense. Not capitalized. No period at the end.
  │       │
  │       └─⫸ Commit Scope: android|android-core
  │
  └─⫸ Commit Type: build|ci|docs|feat|fix|perf|refactor|chore|test
```

The `<type>` and `<summary>` fields are mandatory, the `(<scope>)` field is optional.


##### Type

Must be one of the following:

* **build**: Changes that affect the build system or external dependencies (example scopes: gulp, broccoli, npm)
* **ci**: Changes to our CI configuration files and scripts (examples: CircleCi, SauceLabs)
* **docs**: Documentation only changes
* **feat**: A new feature
* **fix**: A bug fix
* **perf**: A code change that improves performance
* **refactor**: A code change that neither fixes a bug nor adds a feature
* **chore**: Housekeeping tasks that doesn't require to be highlighted in the changelog
* **test**: Adding missing tests or correcting existing tests


##### Scope
The scope should be the name of the npm package affected (as perceived by the person reading the changelog generated from commit messages).

The following is the list of supported scopes:

* `asgardeo-android` - Changes to the `main` package.
* `asgardeo-android-core` - Changes to the `core` package.
* `asgardeo-android-core-auth-direct` - Changes to the `core-auth-direct` package.

## Releases

After merging a PR, the release process will be handled by the maintainers. The release process will be as follows:

### For Asgardeo Android SDK

The release process for the Asgardeo Android SDK will be as follows:

1. The maintainers will go through the PRs merged since the last release to identify the changes.
2. The maintainers will go to the following GitHub Actions workflow: [Release](https://github.com/asgardeo/mobile-ui-sdks/actions/workflows/release.yml).
3. The maintainers will trigger the workflow by clicking on the `Run workflow` button and select `android` as the workflow to run.
4. The maintainers will provide which version to bump based on the changes identified.
5. The changes will be published to the [Maven Central repository](https://central.sonatype.com/artifact/io.asgardeo/asgardeo-android). This will take upto 24 hours to reflect the changes in the Maven Central repository.

### For Documentation

The release process for the documentation will be as follows:

1. The maintainers will go through the PRs merged since the last release to identify the changes.
2. The maintainers will go to the following GitHub Actions workflow: [Deploy Documentation](https://github.com/asgardeo/mobile-ui-sdks/actions/workflows/deploy-gh-pages.yml).
3. The maintainers will trigger the workflow by clicking on the `Run workflow` button.
4. The changes will be published to the [GitHub Pages](https://asgardeo.github.io/mobile-ui-sdks/).
