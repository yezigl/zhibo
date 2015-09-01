/**
 * 
 */
var SEP = ',';

Array.prototype.add = function(item) {
    for ( var i = 0; i < this.length; i++) {
        if (this[i] === item) {
            return this;
        }
    }
    this[this.length] = item;
    return this;
}

Array.prototype.indexOf = function(item) {
    for ( var i = 0; i < this.length; i++) {
        if (this[i] == item) {
            return i;
        }
    }
    return -1;
}

Array.prototype.compare = function(other) {
    var diff = [];
    for ( var i = 0; i < this.length; i++) {
        if (other.indexOf(this[i]) < 0) {
            diff.push(this[i]);
        }
    }
    return diff;
}

Array.prototype.remove = function(item) {
    var index = -1;
    for ( var i = 0; i < this.length; i++) {
        if (this[i] == item) {
            index = i;
        }
    }
    var re = this[index];
    if (index >= 0) {
        this.splice(index, 1);
    }
    return re;
}