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
     # temporisation
     for p in {1..30}; do
     echo -ne "#"
     sleep 1
     done
     echo -e " \n"
     echo "Phase Prepare"
     echo -e " \n"
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c prepare
     # temporisation
     for p in {1..30}; do
     echo -ne "#"
     sleep 1
     done
     echo -e " \n"
     echo "Phase Analyze"
     echo -e " \n"     
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c analyze
     # temporisation
     for p in {1..30}; do
     echo -ne "#"
     sleep 1
     done
     echo -e " \n"
     echo "Phase Detect"
     echo -e " \n"
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c detect
     # temporisation
     for p in {1..30}; do
     echo -ne "#"
     sleep 1
     done
     echo -e " \n"
     echo "Phase Restore"
     echo -e " \n"
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c restore
     # temporisation
     for p in {1..30}; do
     echo -ne "#"
     sleep 1
     done
     echo -e " \n"
     echo "Phase Build"
     echo -e " \n"
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c build
     # temporisation
     for p in {1..30}; do
     echo -ne "#"
     sleep 1
     done
     echo -e " \n"
     echo "Phase Export"
     echo -e " \n"
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c export
     # temporisation
     for p in {1..30}; do
     echo -ne "#"
     sleep 1
     done
     echo -e " \n"
     echo "Phase Completion"
     echo -e " \n"
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c completion
     exit
fi
if [[ "${1}" == "remove" ]]; then
     echo "Suppression de l'image...."
     kubectl delete -f cnam-image-beneficiaires.yaml
     exit
fi

if [[ "${1}" == "logs" ]]; then
     # if [[ -z "${2}" ]]; then
     #      echo "indiquer un pod de build"
     #      exit
     # fi
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c prepare
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c analyze
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c detect
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c restore
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c build
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c export
     kubectl logs --follow -n default cnam-image-beneficiaires-build-1-build-pod -c completion
     exit
fi