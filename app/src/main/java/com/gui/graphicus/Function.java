/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gui.graphicus;

import java.io.IOException;
import java.util.Map;

public class Function {
    private static enum Operation implements FunctionInterface<Cell>{
        add {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return arg1.getResult(vars) + arg2.getResult(vars);
            }
        },
        sub {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return arg1.getResult(vars) - arg2.getResult(vars);
            }
        },
        mul {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return arg1.getResult(vars) * arg2.getResult(vars);
            }
        },
        div {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return arg1.getResult(vars) / arg2.getResult(vars);
            }
        },
        pow {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return Math.pow(arg1.getResult(vars), arg2.getResult(vars));
            }
        },
        sin {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return Math.sin(arg1.getResult(vars));
            }
        },
        cos {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return Math.cos(arg1.getResult(vars));
            }
        },
        tan {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return Math.tan(arg1.getResult(vars));
            }
        },
        log {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return Math.log(arg1.getResult(vars));
            }
        },
        exp {
            @Override
            public double getResult(Cell arg1, Cell arg2, Map<String, Double> vars) {
                return Math.exp(arg1.getResult(vars));
            }
        };

        public static boolean isOperation(char op) {
            return null != toOperation(op);
        }

        public static Operation toOperation(char op) {
            switch (op) {
                case '+':
                    return add;
                case '-':
                    return sub;
                case '*':
                    return mul;
                case '/':
                    return div;
                case '^':
                    return pow;
                default:
                    return null;
            }
        }

        public static boolean isFunction(String func) {
            return null != toFunction(func);
        }

        public static Operation toFunction(String func) {
            if (func.length() < 3) return null;
            switch(func.substring(0, 3)) {
                case "sin":
                    return sin;
                case "cos":
                    return cos;
                case "tan":
                    return tan;
                case "log":
                    return log;
                case "exp":
                    return exp;
                default:
                    return null;
            }
        }

        public static Cell parce(String exp) throws IOException {
            Cell root;

            root = parceAddOperation(exp);
            if (null != root) return root;

            root = parceMulOperation(exp);
            if (null != root) return root;

            root = parcePowOperation(exp);
            if (null != root) return root;

            root = parceFooOperation(exp);
            if (null != root) return root;

            root = parceVarOperation(exp);
            if (null != root) return root;

            root = parceBrakets(exp);
            if (null != root) return root;

            root = parceNumOperation(exp);
            if (null != root) return root;

            //никогда не выполнится
            return null;
        }

        private static Cell parceAddOperation(String exp) throws IOException {
            StringBuilder sb = new StringBuilder();
            if (')' == exp.charAt(0)) throw new IOException();
            int brakets = exp.charAt(0) == '(' ? 1 : 0;
            sb.append(exp.charAt(0));
            for (int i = 1; i < exp.length(); i++) {
                char curr = exp.charAt(i);
                switch (curr) {
                    case '+':
                        if (brakets == 0)
                            return new Cell(parce(sb.toString()), parce(exp.substring(i + 1, exp.length())), add);
                    case '-':
                        if (brakets == 0)
                            return new Cell(parce(sb.toString()), parce(exp.substring(i + 1, exp.length())), sub);
                    case '(':
                        brakets++;
                        sb.append(curr);
                        break;
                    case ')':
                        if (brakets <= 0) throw new IOException();
                        brakets--;
                    default:
                        sb.append(curr);

                }
            }
            return null;
        }

        private static Cell parceMulOperation(String exp) throws IOException {
            StringBuilder sb = new StringBuilder();
            int brakets = 0;
            for (int i = 0; i < exp.length(); i++) {
                char curr = exp.charAt(i);
                switch (curr) {
                    case '*':
                        if (brakets == 0)
                            return new Cell(parce(sb.toString()), parce(exp.substring(i + 1, exp.length())), mul);
                    case '/':
                        if (brakets == 0)
                            return new Cell(parce(sb.toString()), parce(exp.substring(i + 1, exp.length())), div);
                    case '(':
                        brakets++;
                        sb.append(curr);
                        break;
                    case ')':
                        if (brakets <= 0) throw new IOException();
                        brakets--;
                    default:
                        sb.append(curr);

                }
            }
            return null;
        }

        private static Cell parcePowOperation(String exp) throws IOException {
            StringBuilder sb = new StringBuilder();
            int brakets = 0;
            for (int i = 0; i < exp.length(); i++) {
                char curr = exp.charAt(i);
                switch (curr) {
                    case '^':
                        if (brakets == 0)
                            return new Cell(parce(sb.toString()), parce(exp.substring(i + 1, exp.length())), pow);
                    case '(':
                        brakets++;
                        sb.append(curr);
                        break;
                    case ')':
                        if (brakets <= 0) throw new IOException();
                        brakets--;
                    default:
                        sb.append(curr);

                }
            }
            return null;
        }

        private static Cell parceFooOperation(String exp) throws IOException {
            Operation op = toFunction(exp);
            if (null == op) return null;
            return new Cell(parce(exp.substring(4, exp.length() - 1)), null, op);
        }

        private static Cell parceVarOperation(String exp) throws IOException {
            if (exp.length() == 1 && "x".equals(exp)) return new Cell(exp);
            return null;
        }

        private static Cell parceNumOperation(String exp) throws IOException {
            return new Cell(Double.parseDouble(exp));
        }

        private static Cell parceBrakets(String exp) throws IOException {
            if ('(' != exp.charAt(0)) return null;

            int brakets = 1;
            char curr = exp.charAt(1);
            int i;

            for (i = 1; i < exp.length() && 0 != brakets; i++) {
                switch(curr = exp.charAt(i)) {
                    case '(':
                        brakets++;
                        break;
                    case ')':
                        brakets--;
                        break;
                }
            }
            if (i != exp.length()) return null;

            return parce(exp.substring(1, exp.length() - 1));
        }
    }

    private static class Cell {
        private Cell first;
        private Cell second;
        private String var;
        private Double num;
        private Operation doing;

        public Cell(Double num) {
            this.num = num;
        }

        public Cell(String var) {
            this.var = var;
        }

        public Cell(Cell first, Cell second, Operation doing) {
            this.first = first;
            this.second = second;
            this.doing = doing;
        }


        public double getResult(Map<String, Double> vars) {
            if (null != first) return doing.getResult(first, second, vars);
            else if (null != var) return vars.get(var);
            else return num;
        }
    }

    private Cell root;
    private Map<String, Double> vars;

    public Double getFunctionValue() throws IOException {
        return root.getResult(vars);
    }

    public Double valueOf(String var) {
        return vars.get(var);
    }

    public void setVars(Map<String, Double> vars) {
        this.vars = vars;
    }

    public void parce(String exp) throws IOException {
        exp = exp.replace(" ", "");
        exp = exp.replace("\t", "");
        root = Operation.parce(exp);
    }
}