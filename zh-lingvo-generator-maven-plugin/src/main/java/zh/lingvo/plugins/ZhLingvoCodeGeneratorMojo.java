package zh.lingvo.plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import zh.lingvo.config.generators.LingvoDescriptionGenerator;

import java.io.IOException;

@Mojo( name = "generate-zh-lingvo-code", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ZhLingvoCodeGeneratorMojo extends AbstractMojo {
    @Parameter(property = "outputDir", required = true)
    private String outputDirectory;

    @Parameter(property = "basePackage", required = true)
    private String basePackage;

    @Parameter(property = "config", required = true)
    private String configPath;

    public void execute() throws MojoExecutionException {
        getLog().info("Starting generating Zh.Lingvo code.");
        try {
            LingvoDescriptionGenerator.LOG_INFO = str -> getLog().info(str);
            LingvoDescriptionGenerator.LOG_ERROR = str -> getLog().error(str);
            LingvoDescriptionGenerator.main(new String[]{
                    "-o", outputDirectory,
                    "-p", basePackage,
                    configPath
            });
            getLog().info("Generating Zh.Lingvo code completed.");
        } catch (IOException e) {
            getLog().error("Error while generating Zh.Lingvo code.");
            throw new MojoExecutionException(e);
        }
    }
}
