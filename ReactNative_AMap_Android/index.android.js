/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component, PropTypes} from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TouchableHighlight
} from 'react-native';

import { NativeModules } from 'react-native';
var AMapLocationAPI = NativeModules.AMapLocationAPI;

class CustomButton extends React.Component {
  render() {
    return (
      <TouchableHighlight
        style={styles.button}
        underlayColor="#a5a5a5"
        onPress={this.props.onPress}>
        <Text style={styles.buttonText}>{this.props.text}</Text>
      </TouchableHighlight>
    );
  }
}

export default class ReactNative_AMap_Android extends Component {
  constructor(props){
    super(props);
    this.state={
        result:'',
        location:''
    }
  }

  render() {
    return (
      <View style={styles.container}>

        <CustomButton text="点击调用原生模块getLocation方法"
            onPress={()=>AMapLocationAPI.getLocation("{'onceLocation':false,'needAddress':true,'interval':2000}",
            (errorCode, locData)=>{
              // this.setState({location:JSON.stringify(data)});
              var locResult = JSON.parse(locData);
              if(errorCode != 0){
                this.setState({result:"定位失败",location:locResult});
              } else {
                this.setState({result:"定位成功",location:locResult});
              }
              }
            )}
        />

        <CustomButton text="点击调用原生模块stopLocation方法"
            onPress={()=>AMapLocationAPI.stopLocation()}
        />

        <Text>
          定位结果：{this.state.result},{this.state.location.errorInfo}{'\n'}
          经纬度：{this.state.location.latitude},{this.state.location.longitude}{'\n'}
          Address:{this.state.location.address}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('ReactNative_AMap_Android', () => ReactNative_AMap_Android);
