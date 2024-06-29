Label Verifier plugin for Jenkins
=================================

This plugin allows system administrators to programmatically verify the label assignment correctness on agents. It is useful to prevent a human error in label assignment when you have a larger number or self-organizing agents and generally as a means to make sure your Jenkins agent labels are correct.

## Usage

Go to the label configuration page of the label whose assignment you want to validate. For example, http://yourserver/jenkins/label/foo/configure.
You can associate "label verifies" through this UI, as follows:

![Verifier Configuration](/docs/images/config.png)

The specified script is executed every time an agent with this label comes online.
If the script returns a non-zero exit code, the label assignment is considered illegal and Jenkins will mark the agent as offline to prevent it from being used for a build.

## Changelog

See [GitHub Releases](https://github.com/jenkinsci/label-verifier-plugin/releases)
