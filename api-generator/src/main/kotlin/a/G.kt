package a

import freemarker.template.Configuration
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.*
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.Serializable
import java.nio.file.StandardOpenOption
import kotlin.io.path.createDirectories
import kotlin.io.path.outputStream

private const val DEFAULT_TASK_NAME = "ftlGenerate"
private const val DEFAULT_OUTPUT = "ftl/generated/main/java"

class G : Plugin<Project> {
    override fun apply(project: Project) {
        val j = project.extensions.getByType(JavaPluginExtension::class.java) ?: return
        j.sourceSets.getByName("main").java.srcDir(project.layout.buildDirectory.dir(DEFAULT_OUTPUT))
        val task = project.tasks.create(DEFAULT_TASK_NAME, GenTask::class.java)
        project.afterEvaluate {
            project.tasks.withType(KotlinCompile::class.java) {
                it.dependsOn(task)
            }
            project.tasks.withType(JavaCompile::class.java) {
                it.dependsOn(task)
            }
            try {
                project.tasks.getByName("sourcesJar") {
                    it.dependsOn(task)
                }
            }catch (_: UnknownDomainObjectException){}
            project.tasks.withType(Javadoc::class.java) {
                it.dependsOn(task)
            }
        }
    }
}

internal data class RenderItem(
    val template: String,
    val target: String,
    val data: Any,
) : Serializable

class FtlGenerationConfigBuilder {
    internal lateinit var renderList2: ListProperty<RenderItem>
    fun render(template: String, target: String, data: Any) {
        renderList2.add(RenderItem(template, target, data))
    }

}

fun Project.ftlGenerate(block: FtlGenerationConfigBuilder.() -> Unit) {
    val task = tasks.getByName(DEFAULT_TASK_NAME) as GenTask
    val configBuilder = FtlGenerationConfigBuilder()
    configBuilder.renderList2 = task.renderItems
    block.invoke(configBuilder)
}

abstract class GenTask : DefaultTask() {
    @get:InputDirectory
    @get:SkipWhenEmpty
    abstract val templateDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get: Input
    internal abstract val renderItems: ListProperty<RenderItem>

    init {
        templateDir.convention(project.layout.projectDirectory.dir("templates"))
        outputDir.convention(project.layout.buildDirectory.dir(DEFAULT_OUTPUT))
        renderItems.convention(emptyList())
    }

    @TaskAction
    fun doRendering() {
        logger.info("start rendering: {} -> {}", templateDir, outputDir)
        val cfg = Configuration(Configuration.VERSION_2_3_32)
        cfg.setDirectoryForTemplateLoading(templateDir.get().asFile)
        renderItems.get().forEach {
            logger.info(">>>> rendering: {} -> {}", it.template, it.target)
            val targetFile = outputDir.get().file(it.target).asFile.toPath()
            targetFile.parent.createDirectories()
            targetFile.outputStream(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING).bufferedWriter()
                .use { w ->
                    cfg.getTemplate(it.template).process(it.data, w)
                }
        }
    }
}
