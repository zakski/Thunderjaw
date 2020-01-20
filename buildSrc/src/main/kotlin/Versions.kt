import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

/**
 * Generated by https://github.com/jmfayard/buildSrcVersions
 *
 * Find which updates are available by running
 *     `$ ./gradlew buildSrcVersions`
 * This will only update the comments.
 *
 * YOU are responsible for updating manually the dependency version.
 */
object Versions {
    const val org_jetbrains_kotlinx_kotlinx_coroutines: String = "1.3.0-RC2"
    // available: "1.3.3"

    const val io_github_javaeden_orchid: String = "0.18.1"

    const val org_jetbrains_kotlin: String = "1.3.61"

    const val io_github_gciatto: String = "0.0.6"

    const val org_danilopianini_git_sensitive_semantic_versioning_gradle_plugin: String = "0.2.2"

    const val org_jetbrains_kotlin_multiplatform_gradle_plugin: String = "1.3.61"

    const val de_fayard_buildsrcversions_gradle_plugin: String = "0.7.0"

    const val com_eden_orchidplugin_gradle_plugin: String = "0.18.1"

    const val org_jetbrains_dokka_gradle_plugin: String = "0.10.0"

    const val com_jfrog_bintray_gradle_plugin: String = "1.8.4"

    /**
     * Current version: "6.0.1"
     * See issue 19: How to update Gradle itself?
     * https://github.com/jmfayard/buildSrcVersions/issues/19
     */
    const val gradleLatestVersion: String = "6.1"
}

/**
 * See issue #47: how to update buildSrcVersions itself
 * https://github.com/jmfayard/buildSrcVersions/issues/47
 */
val PluginDependenciesSpec.buildSrcVersions: PluginDependencySpec
    inline get() =
        id("de.fayard.buildSrcVersions").version(Versions.de_fayard_buildsrcversions_gradle_plugin)
