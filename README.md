# mq-gitops-samples
GitOps samples for IBM MQ

The Kustomize deployment samples in this repository are intended for use with an implementation of ArgoCD. The sample in the 'queue-manager-deployment' folder will deploy a two queue manager uniform-cluster with TLS and a dynamic MQSC update capability. The sample has a custom sed transformer that provides the capability to template any files that Kustomize generates, including the MQSC that gets inserted into Kubernetes config maps.

**Prerequisites:**

An OpenShift cluster with a default storage class and the following Operators:

- IBM MQ
- Red Hat OpenShift GitOps (ensure ArgoCD has access to the terget project and plugins are enabled)
- cert-manager Operator for Red Hat OpenShift

Patch ArgoCD to allow the use of plugins, first option applies globally, second has application scope.

option 1

```yaml
apiVersion: argoproj.io/v1alpha1
kind: ArgoCD
metadata:
  name: openshift-gitops
  namespace: openshift-gitops
spec:
  repo:
  name: kustomize
  kustomizeBuildOptions: '--enable-alpha-plugins=true --enable-exec'
```
option 2
```yaml
apiVersion: argoproj.io/v1alpha1
kind: ArgoCD
metadata:
  name: openshift-gitops
  namespace: openshift-gitops
spec:
  repo:
  name: kustomize
  configManagementPlugins: |
    - name: kustomize-build-with-params
      generate:
        command: [ "sh", "-c" ]
        args: ["kustomize build --enable-alpha-plugins=true --enable-exec" ]
```
For option 2 add this to your ArgoCD Application YAML for application scoped plugin enablement,
```yaml
plugin:
  name: kustomize-build-with-params
```

example role binding

```yaml
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: argo-test
  namespace: default
subjects:
  - kind: ServiceAccount
    name: openshift-gitops-argocd-application-controller
    namespace: openshift-gitops
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: admin
```

**Usage:**

Deploy the argo-app.yaml 

    oc apply -f argo-app.yaml


Disclaimer

All samples in this repository are provided AS-IS without warranty of any kind, express or implied.  IBM shall not be responsible for any damages arising out of the use of, or otherwise related to, these samples.
