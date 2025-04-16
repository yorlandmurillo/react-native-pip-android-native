import {useContext, useEffect} from 'react';
import {NativeEventEmitter, NativeModules, AppState, Text} from 'react-native';

// import {PipContext} from '../context/pipContext';

const PipScreen = ({}) => {
  //   const {
  //     pipActive,
  //     setPipActive,
  //     setPipVideo,
  //     pipVideo,
  //     callPipToClose,
  //     setCallPipToClose,
  //   }: any = useContext(PipContext);
  const {NativePipModule} = NativeModules;
  const pipEventEmitter = new NativeEventEmitter(NativePipModule);

  useEffect(() => {
    const subscription = pipEventEmitter.addListener('onExitPip', () => {
      //si esta en background levantar la app llamando a una funcion nativa
      if (AppState.currentState == 'background') {
        NativePipModule.bringAppToFront();
      }

      //   setPipActive(false);
    });

    const pipDestroyedSubscription = pipEventEmitter.addListener(
      'onPipDestroyed',
      () => {
        // O manejar la destrucción de la actividad de PiP si es necesario.
        console.log('La actividad PiP se destruyó');
        // setPipActive(false);
      },
    );

    return () => {
      subscription.remove();
      pipDestroyedSubscription.remove();
    };
  }, []);

  //   useEffect(() => {
  //     if (pipActive) {
  //       //activate de Picture in Picture
  //       if (pipVideo != null) {
  //         NativePipModule?.showPIP(pipVideo);
  //       } else {
  //         //there is no video to show, maybe format, talk with Damian
  //         setPipActive(false);
  //       }
  //     }
  //   }, [pipActive, pipVideo]);

  //   useEffect(() => {
  //     if (callPipToClose) {
  //       //close the PiP
  //       NativePipModule.closeImmergoPip();
  //       setCallPipToClose(false); //seteamos nuevamente el llamado al cerrar el pip en false
  //     }
  //   }, [callPipToClose]);

  return (
    <Text
      onPress={() => {
        console.log('pasa');
      }}>
      Click here to PiP
    </Text>
  );
};

export default PipScreen;
