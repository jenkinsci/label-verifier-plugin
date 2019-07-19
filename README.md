Label Verifier plugin for Jenkins
=================================

This plugin allows system administrator to programmatically verify the label assignment correctness on agents.It is useful to prevent a human error in label assignment when you have a larger number or self-organizing agents, and generally as a means to make sure your Jenkins cluster is healthy.

## Usage

Go to the label configuration page of the label whose assignment you want to validate. For example, http://yourserver/jenkins/label/foo/configure.
You can associate "label verifies" through this UI, as follows:

[![Verifier Configuration](/docs/images/config.png)]

The script specified here gets executed every time an agent with this label comes online. 
If the script returns a non-zero exit code, the label assignment is considered illegal, and Jenkins will mark the agent as offline to prevent it from getting used for a build.

## Extension points

`LabelVerifier` is an extension point that can be implemented by other plugins, to perform the check in other means.
See [this page](https://jenkins.io/doc/developer/extensions/label-verifier/#labelverifier) to get a list of existing implementations.
