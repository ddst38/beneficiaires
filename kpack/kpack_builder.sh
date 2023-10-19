#!/usr/bin/bash

if [[ -z "${1}" ]]; then
     echo "indiquer install|uninstall|build|remove"
     exit
fi

if [[ "${1}" == "install" ]]; then
     echo "Installation de la configuration de build kpack..."
     kubectl apply -f cnam-registry-credentials.yaml
     kubectl apply -f cnam-service-account.yaml
     kubectl apply -f cnam-cluster-store.yaml
     kubectl apply -f cnam-cluster-stack.yaml
     kubectl apply -f cnam-image-builder.yaml

     exit 
fi
if [[ "${1}" == "uninstall" ]]; then
     echo "Installation de la configuration de build kpack..."
     kubectl delete -f cnam-registry-credentials.yaml
     kubectl delete -f cnam-service-account.yaml
     kubectl delete -f cnam-cluster-store.yaml
     kubectl delete -f cnam-cluster-stack.yaml
     kubectl delete -f cnam-image-builder.yaml
     exit
fi

if [[ "${1}" == "build" ]]; then
     echo "Build de l'image...."
     kubectl apply -f cnam-image-beneficiaires.yaml
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c build
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c export
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c completion
     exit
fi
if [[ "${1}" == "remove" ]]; then
     echo "Suppression de l'image...."
     kubectl delete -f cnam-image-builder.yaml
     exit
fi
if [[ "${1}" == "patch" ]]; then
     if [[ -z "${2}" ]]; then
          echo "indiquer un id de revision"
          exit
     fi
     echo "Patch de l'image...."
     -p '{"spec":{"unschedulable":true}}'
     kubectl patch -f cnam-image-builder.yaml -p '{"spec":{"source":{"git":{"revision":"${2}"}}}'
     exit
fi